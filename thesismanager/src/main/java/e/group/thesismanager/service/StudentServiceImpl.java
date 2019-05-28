package e.group.thesismanager.service;

import e.group.thesismanager.exception.DeadlinePassed;
import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final ThesisRepository thesisRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final SemesterService semesterService;

    public StudentServiceImpl(ThesisRepository thesisRepository,
                              SubmissionRepository submissionRepository,
                              UserRepository userRepository, SemesterService semesterService) {

        this.thesisRepository = thesisRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.semesterService = semesterService;
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

    //Gets the active thesis
    @Override
    public Thesis getThesisByStudentId(Long studentId) {
        return thesisRepository.findThesisByStudentIdAndSemesterActiveIsTrue(studentId)
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

    //Is student allowed to edit/add a Submission(considering criteria such as deadline)
    @Override
    public Boolean isSubmissionAllowed(Long studentId, SubmissionType submissionType) {

        User student = userRepository.findUserByIdAndRolesContaining(studentId, Role.ROLE_STUDENT).orElseThrow(() ->
                new NotFoundException("Student does not exist. Student Id:" + studentId));

        switch (submissionType) {
            case PROJECT_DESCRIPTION:
                return isProjectDescriptionSubmissionAllowed(student);
            case PROJECT_PLAN:
                return isProjectPlanSubmissionAllowed(student);
            case REPORT:
                return isReportSubmissionAllowed(student);
            case FINAL_REPORT:
                return isFinalReportSubmissionAllowed(student);
            default:
                log.error("No such role exists.");
                return false;
        }
    }

    //Get the list of Submissions that the student is allowed to edit/add
    @Override
    public List<SubmissionType> getAllowedSubmissionTypes(Long studentId) {
        User student = userRepository.findUserByIdAndRolesContaining(studentId, Role.ROLE_STUDENT).orElseThrow(() ->
                new NotFoundException("Student does not exist. Student Id:" + studentId));

        List<SubmissionType> result = new LinkedList<>();
        if(!thesisRepository.findThesisByStudentIdAndSemesterActiveIsTrue(studentId).isPresent())
            return result;

        if(isProjectDescriptionSubmissionAllowed(student))
            result.add(SubmissionType.PROJECT_DESCRIPTION);
        if(isProjectPlanSubmissionAllowed(student))
            result.add(SubmissionType.PROJECT_PLAN);
        if(isReportSubmissionAllowed(student))
            result.add(SubmissionType.REPORT);
        if(isFinalReportSubmissionAllowed(student))
            result.add(SubmissionType.FINAL_REPORT);

        return result;
    }

    private Boolean isProjectDescriptionSubmissionAllowed(User student) {

        return true;
    }

    private Boolean isProjectPlanSubmissionAllowed(User student) {

        return true;
    }

    private Boolean isReportSubmissionAllowed(User student) {

        return true;
    }
    private Boolean isFinalReportSubmissionAllowed(User student) {

        return true;
    }

    private void submitDocument(Thesis thesis, Document document, SubmissionType type){

        if (!semesterService.isDeadlinePassed(type)) {
            Submission submission = new Submission(thesis, type);
            submission.setType(type);
            submission.setSubmittedDocument(document);
            thesis.addSubmission(submission);
            thesisRepository.save(thesis);
        }
        else
            throw new DeadlinePassed("Deadline is passed.");

    }

    @Override
    public Submission submitDocument(Long studentId, String comment, MultipartFile multipartFile
            , SubmissionType submissionType) throws IOException {
        Thesis thesis = thesisRepository.findThesisByStudentIdAndSemesterActiveIsTrue(studentId).orElseThrow(() ->
                new NotFoundException("No thesis exists for student ID : " + studentId));
        if (!isSubmissionAllowed(studentId, submissionType))
            throw new RuntimeException(submissionType + " submission is not allowed for Student ID " + studentId);

        Submission submission = submissionRepository.findByThesisAndType(thesis, submissionType)
                .orElse(new Submission(thesis, submissionType));

        Document document = submission.getSubmittedDocument();

        //adding file
        if(!multipartFile.isEmpty()) {
            byte[] file = new byte[multipartFile.getBytes().length];
            int i = 0;
            for (byte b : multipartFile.getBytes()){
                file[i++] = b;
            }

            document.setFile(file);
            document.setFileName(multipartFile.getOriginalFilename());
            document.setFileType(multipartFile.getContentType());
        }

        document.setComment(comment);
        document.setAuthor(thesis.getStudent());

        return submissionRepository.save(submission);
    }
}