package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface SupervisorService {

    List<Thesis> getThesis();

    List<Thesis> getThesis(User user);

    User getUserByUsername(String username);

    List<Thesis> getRequests(User user);

    Thesis getThesisById(Long id);

    /*Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    Submission feedbackOnSubmission(Long submissionId, String comment, byte[] file, String fileName, String fileType, User author, LocalDateTime submissionTime, Role authorRole);

    Submission feedbackCommentOnSubmission(Long submissionId, String comment, Long authorId, Role authorRole);

    Submission feedbackFileOnSubmission(Long submissionId, MultipartFile multipartFile, Long authorId, Role authorRole) throws IOException;

    Submission editFeedbackOnSubmission(Long submissionId, Long feedbackId, String updatedComment, byte[] file, String fileName, String fileType, User updatedAuthor,
                                        LocalDateTime updatedSubmissionTime, Role updatedAuthorRole);

    Submission assessSubmission(Long submissionId, Float grade);

    Submission editAssessmentSubmission(Long submissionId, Float updatedGrade);*/

    Thesis replyOnSupervisionProposition(Long thesisId, User supervisor, SupervisorRequestStatus answer)throws MissingRoleException;
}