package e.group.thesismanager.service;

import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    Thesis initThesis(User student, Semester semester) throws MissingRoleException;

    Thesis getThesisById(Long id);

    Thesis getThesis(User student, Semester semester);

    Thesis getThesisByStudentId(Long studentId);

    List<Thesis> getTheses(User student);

    List<Thesis> getThesesByStudentId(Long studentId);

    //Returns list of all supervisors. If a supervisor has accepted a request for this student
    // in the active semester the list will only contain that supervisor.
    List<User> getSupervisors(Thesis thesis);

    void proposeSupervisor(Thesis thesis, User supervisor) throws MissingRoleException, InvalidSupervisorRequestException;

    void submitProjectDescription(Thesis thesis, Document projectDescription);

    void submitProjectPlan(Thesis thesis, Document projectPlan);

    void submitReport(Thesis thesis, Document report);

    void submitFinalReport(Thesis thesis, Document finalReport);

    Boolean isSubmissionAllowed(Long studentId, SubmissionType submissionType);

    List<SubmissionType> getAllowedSubmissionTypes(Long studentId);

    Submission submitDocument(Long studentId, String comment, MultipartFile multipartFile
            , SubmissionType submissionType) throws IOException;
}