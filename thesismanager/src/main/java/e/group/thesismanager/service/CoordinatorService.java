package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public interface CoordinatorService {

    List<Thesis> getThesis();

    Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    Submission feedbackOnSubmission(Long submissionId, String comment, byte[] file, String fileName, String fileType, User author, LocalDateTime submissionTime, Role authorRole);

    Submission assessSubmission(Long submissionId, Float grade);

    Thesis assignOpponent(User student, User opponent) throws MissingRoleException;

    Thesis assignSupervisor(User student, User supervisor) throws MissingRoleException;

    Thesis evaluateProjectPlan(User student, Float grade) throws MissingRoleException;

    Thesis gradeFinalProject(User student, Float grade) throws MissingRoleException;

    Thesis initiateThesis(User student) throws MissingRoleException;

    void setThesis(List<Thesis> thesisList);

    Semester initSemester(Year Year, SemesterPeriod semesterPeriod);

    void setDeadline(SubmissionType type, LocalDateTime dateTime);
}
