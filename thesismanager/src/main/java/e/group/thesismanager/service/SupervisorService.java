package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface SupervisorService {

    List<Thesis> getThesis();

    List<Thesis> getThesis(User user);

    List<Thesis> getRequests(User user);

    Thesis getThesisById(Long id);

    Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    Submission feedbackOnSubmission(Long submissionId, String comment, File file, User author, LocalDateTime submissionTime, Role authorRole);

    Submission editFeedbackOnSubmission(Long submissionId, Long feedbackId, String updatedComment, File updatedFile, User updatedAuthor,
                                        LocalDateTime updatedSubmissionTime, Role updatedAuthorRole);

    Submission assessSubmission(Long submissionId, Float grade);

    Submission editAssessmentSubmission(Long submissionId, Float updatedGrade);

    Thesis replyOnSupervisionProposition (Long thesisId, User supervisor, boolean answer) throws MissingRoleException;
}