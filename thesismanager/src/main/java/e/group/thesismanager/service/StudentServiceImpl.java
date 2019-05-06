package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final ThesisRepository thesisRepository;
    private final SubmissionRepository submissionRepository;

    @Autowired
    public StudentServiceImpl(ThesisRepository thesisRepository, SubmissionRepository submissionRepository) {
        this.thesisRepository = thesisRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public void initThesis(User student, Semester semester) {
        Thesis thesis = new Thesis();
        thesis.setStudent(student);
        thesis.setSemester(semester);
        thesisRepository.save(thesis);
    }

    @Override
    public Thesis getThesis(Long id) {
        return thesisRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist. Id: "+id));
    }

    @Override
    public Thesis getThesis(User student, Semester semester) {
        List<Thesis> theses = thesisRepository.findThesesByStudentAndSemester(student, semester);
        if(theses.size() == 0)
            throw new NotFoundException("Thesis not found. Student Id: "+student.getId()+" Semester Id: "+semester.getId());

        return theses.get(0);
    }

    @Override
    public void proposeSupervisor(Thesis thesis, User supervisor) {
        thesis.setSupervisor(supervisor);
        thesis.setSupervisorAccept(false);
        thesisRepository.save(thesis);
    }

    @Override
    public void submitProjectDescription(Thesis thesis, Document projectDescription) {
        submitDocument(thesis, projectDescription, SubmissionType.PROJECT_DESCRIPTION);
    }

    @Override
    public void submitProjectPlan(Thesis thesis, Document projectPlan) {
        submitDocument(thesis, projectPlan, SubmissionType.PROJECT_PLAN);
    }

    @Override
    public void submitReport(Thesis thesis, Document report) {
        submitDocument(thesis, report, SubmissionType.REPORT);
    }

    @Override
    public void submitFinalReport(Thesis thesis, Document finalReport) {
        submitDocument(thesis, finalReport, SubmissionType.FINAL_REPORT);
    }

    private void submitDocument(Thesis thesis, Document document, SubmissionType type){
        Submission submission = new Submission();
        submission.setType(type);
        submission.setSubmittedDocument(document);
        thesis.getSubmissions().add(submission);

        submissionRepository.save(submission);
        thesisRepository.save(thesis);
    }
}
