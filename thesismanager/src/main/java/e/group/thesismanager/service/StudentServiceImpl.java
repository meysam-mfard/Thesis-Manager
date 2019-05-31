package e.group.thesismanager.service;

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
import java.time.LocalDateTime;
import java.util.Collections;
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

    //Returns list of all supervisors. If a supervisor has accepted a request for this student
    // in the active semester the list will only contain that supervisor.
    // If thesis is not initiated or project description is not passed, returns an empty list
    @Override
    public List<User> getSupervisors(Thesis thesis) {
        if (thesis == null)
            return Collections.emptyList();

        if (thesis.getSupervisorRequestStatus().equals(SupervisorRequestStatus.ACCEPTED))
            return Collections.singletonList(thesis.getSupervisor());
        else if(semesterService.isDeadlinePassed(SubmissionType.PROJECT_PLAN))
            return Collections.emptyList();

        //Supervisor selection is not possible if project description is not passed
        if ((!thesis.getSubmissionByType(SubmissionType.PROJECT_DESCRIPTION).isPresent())
                || thesis.getSubmissionByType(SubmissionType.PROJECT_DESCRIPTION).get()
                .getGradeOptional().orElse(0F).equals(0F))
            return Collections.emptyList();

        return userRepository.findAllByRolesContaining(Role.ROLE_SUPERVISOR);
    }

    @Override
    public void proposeSupervisor(Thesis thesis, User supervisor) throws MissingRoleException
            , InvalidSupervisorRequestException {

        if(!supervisor.getRoles().contains(Role.ROLE_SUPERVISOR))
            throw new MissingRoleException("Could not propose supervisor; Proposed user is not a supervisor");

        if(thesis.getSupervisorRequestStatus() == SupervisorRequestStatus.ACCEPTED)
            throw new InvalidSupervisorRequestException("Could not propose supervisor; A supervisor has already accepted");

        thesis.setSupervisor(supervisor);
        thesis.setSupervisorRequestStatus(SupervisorRequestStatus.REQUEST_SENT);
        thesisRepository.save(thesis);
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
            document.setSubmissionTime(LocalDateTime.now());
        }

        if (comment.length()!=0) {
            document.setComment(comment);
            document.setSubmissionTime(LocalDateTime.now());
        }
        document.setAuthor(thesis.getStudent());

        return submissionRepository.save(submission);
    }

    //Is student allowed to edit/add a Submission(considering criteria such as deadline)
    @Override
    public Boolean isSubmissionAllowed(Long studentId, SubmissionType submissionType) {

        // Check if student exist
        userRepository.findUserByIdAndRolesContaining(studentId, Role.ROLE_STUDENT).orElseThrow(() ->
                new NotFoundException("Student does not exist. Student Id:" + studentId));

        Thesis thesis = thesisRepository.findThesisByStudentIdAndSemesterActiveIsTrue(studentId).orElse(null);
        //If student does not have an active thesis
        if(thesis == null)
            return false;

        //If final report is already graded
        if(thesis.getSubmissionByType(SubmissionType.FINAL_REPORT).isPresent()
                && thesis.getSubmissionByType(SubmissionType.FINAL_REPORT).get().getGradeOptional().isPresent())
            return false;

        switch (submissionType) {
            case PROJECT_DESCRIPTION:
                return isProjectDescriptionSubmissionAllowed(thesis);
            case PROJECT_PLAN:
                return isProjectPlanSubmissionAllowed(thesis);
            case REPORT:
                return isReportSubmissionAllowed(thesis);
            case FINAL_REPORT:
                return isFinalReportSubmissionAllowed(thesis);
            default:
                log.error("No such submission type exists.");
                return false;
        }
    }

    //Get the list of Submissions that the student is allowed to edit/add
    @Override
    public List<SubmissionType> getAllowedSubmissionTypes(Long studentId) {

        // Check if student exist
        userRepository.findUserByIdAndRolesContaining(studentId, Role.ROLE_STUDENT).orElseThrow(() ->
                new NotFoundException("Student does not exist. Student Id:" + studentId));

        List<SubmissionType> result = new LinkedList<>();
        Thesis thesis = thesisRepository.findThesisByStudentIdAndSemesterActiveIsTrue(studentId).orElse(null);
        //If student does not have an active thesis
        if(thesis == null)
            return result;

        //If final report is already graded
        if(thesis.getSubmissionByType(SubmissionType.FINAL_REPORT).isPresent()
                && thesis.getSubmissionByType(SubmissionType.FINAL_REPORT).get().getGradeOptional().isPresent())
            return result;

        if(isProjectDescriptionSubmissionAllowed(thesis))
            result.add(SubmissionType.PROJECT_DESCRIPTION);
        if(isProjectPlanSubmissionAllowed(thesis))
            result.add(SubmissionType.PROJECT_PLAN);
        if(isReportSubmissionAllowed(thesis))
            result.add(SubmissionType.REPORT);
        if(isFinalReportSubmissionAllowed(thesis))
            result.add(SubmissionType.FINAL_REPORT);

        return result;
    }

    private Boolean isProjectDescriptionSubmissionAllowed(Thesis thesis) {
        if (semesterService.isDeadlinePassed(SubmissionType.PROJECT_DESCRIPTION))
            return false;

        //Cannot submit if already passed
        if (thesis.getSubmissionByType(SubmissionType.PROJECT_DESCRIPTION).isPresent()
                && thesis.getSubmissionByType(SubmissionType.PROJECT_DESCRIPTION).get()
                .getGradeOptional().orElse(0F).equals(1F))
            return false;

        return true;
    }

    private Boolean isProjectPlanSubmissionAllowed(Thesis thesis) {
        if (semesterService.isDeadlinePassed(SubmissionType.PROJECT_PLAN))
            return false;

        //Cannot submit if already passed
        if (thesis.getSubmissionByType(SubmissionType.PROJECT_PLAN).isPresent()
                && thesis.getSubmissionByType(SubmissionType.PROJECT_PLAN).get()
                .getGradeOptional().orElse(0F).equals(1F))
            return false;

        //Cannot submit if project description is not passed
        if ( (!thesis.getSubmissionByType(SubmissionType.PROJECT_DESCRIPTION).isPresent()) ||
                thesis.getSubmissionByType(SubmissionType.PROJECT_DESCRIPTION).get()
                        .getGradeOptional().orElse(0F).equals(0F))
            return false;

        return true;
    }

    private Boolean isReportSubmissionAllowed(Thesis thesis) {
        if (semesterService.isDeadlinePassed(SubmissionType.REPORT))
            return false;

        //Cannot submit if project plan is not passed
        if ( (!thesis.getSubmissionByType(SubmissionType.PROJECT_PLAN).isPresent()) ||
                thesis.getSubmissionByType(SubmissionType.PROJECT_PLAN).get()
                        .getGradeOptional().orElse(0F).equals(0F))
            return false;
        return true;
    }
    private Boolean isFinalReportSubmissionAllowed(Thesis thesis) {
        if (semesterService.isDeadlinePassed(SubmissionType.FINAL_REPORT))
            return false;

        //Cannot submit if project plan is not passed
        if (!thesis.getSubmissionByType(SubmissionType.PROJECT_PLAN).isPresent() ||
                thesis.getSubmissionByType(SubmissionType.PROJECT_PLAN).get()
                        .getGradeOptional().orElse(0F).equals(0F))
            return false;
        return true;
    }
}