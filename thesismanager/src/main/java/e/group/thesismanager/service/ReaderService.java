package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface ReaderService {

    List<Thesis> getTheses();

    //Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    //Submission feedbackOnSubmission(Long submissionId, String comment, byte[] file, String fileName, String fileType, User author, LocalDateTime submissionTime, Role authorRole);

    Submission assessSubmission(Long submissionId, Float grade);

    Thesis bidOnThesis(Long thesisId, User reader) throws MissingRoleException;

    User getUserByUsername(String username);
}