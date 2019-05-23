package e.group.thesismanager.service;

import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.SemesterRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final ThesisRepository thesisRepository;
    private final SubmissionRepository submissionRepository;
    private final SemesterRepository semesterRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentServiceImpl(ThesisRepository thesisRepository,
                              SubmissionRepository submissionRepository,
                              SemesterRepository semesterRepository,
                              UserRepository userRepository) {

        this.thesisRepository = thesisRepository;
        this.submissionRepository = submissionRepository;
        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Thesis initThesis(User student, Semester semester) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not initialize thesis; User is not a student");


        Thesis thesis = new Thesis();
        thesis.setStudent(student);
        thesis.setSemester(semester);
        thesis.setSubmissions(new ArrayList<>());
        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis getThesisById(Long id) {

        return thesisRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist. Id: " + id));
    }

    @Override
    public Thesis getThesis(User student, Semester semester) {

        return thesisRepository.findThesisByStudentAndSemester(student, semester).orElseThrow(() ->
                new NotFoundException("Thesis not found. Student Id: " + student.getId() + " Semester Id: " + semester.getId()));
    }

    @Override
    public Thesis getThesisForActiveSemesterByStudentId(Long studentId) {
        return thesisRepository.findThesesByStudentId(studentId).stream()
                .filter(thesis1 -> thesis1.getSemester().isActive()).findFirst()
                .orElseThrow(() -> new NotFoundException("No active thesis for student Id: "+studentId));
    }

    @Override
    public List<Thesis> getTheses(User student) {
        return thesisRepository.findThesesByStudent(student);
    }

    @Override
    public List<Thesis> getThesesByStudentId(Long studentId) {
        return thesisRepository.findThesesByStudentId(studentId);
    }

    @Override
    public List<Semester> getSemesters() {
        return semesterRepository.findAll();
    }

    @Override
    public List<User> getSupervisors() {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if(!u.getRoles().contains(Role.ROLE_SUPERVISOR))
                users.remove(u);
        }
        return users;
    }

    @Override
    public void proposeSupervisor(Thesis thesis, User supervisor) throws MissingRoleException, InvalidSupervisorRequestException {

        if(!supervisor.getRoles().contains(Role.ROLE_SUPERVISOR))
            throw new MissingRoleException("Could not propose supervisor; Proposed user is not a supervisor");

        if(thesis.getSupervisorRequestStatus() == SupervisorRequestStatus.ACCEPTED)
            throw new InvalidSupervisorRequestException("Could not propose supervisor; A supervisor has already accepted");

        thesis.setSupervisor(supervisor);
        thesis.setSupervisorRequestStatus(SupervisorRequestStatus.REQUEST_SENT);
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
        thesis.addSubmission(submission);
        thesisRepository.save(thesis);
    }
}