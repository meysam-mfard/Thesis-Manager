package e.group.thesismanager.service;

import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface OpponentService {

    List<Thesis> getThesis();

    Thesis getThesis(User user);

    User getUserByUsername(String username);

    Thesis getThesisById(Long id);

    //Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    //Submission feedbackOnSubmission(Long submissionId, String comment, byte[] file, String fileName, String fileType, User author, LocalDateTime submissionTime, Role authorRole);

    //Submission editFeedbackOnSubmission(Long submissionId, Long feedbackId, String updatedComment, byte[] file, String fileName, String fileType, User updatedAuthor,LocalDateTime updatedSubmissionTime, Role updatedAuthorRole);

    //Submission assessSubmission(Long submissionId, Float grade);
}