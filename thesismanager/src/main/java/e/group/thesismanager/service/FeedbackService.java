package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.Thesis;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FeedbackService {

    List<Thesis> getThesis();

    Submission getSubmissionById(Long submissionId);

    Submission feedbackOnSubmission(Long submissionId, String comment, MultipartFile multipartFile
            , Long authorId, Role authorRole) throws IOException, MissingRoleException;

    Submission assessSubmission(Long submissionId, Float grade);
}