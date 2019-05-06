package e.group.thesismanager.service;

import e.group.thesismanager.model.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface AssessmentService {

    List<Thesis> getThesis();

    Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    Submission feedbackOnSubmission(Long submissionId, String comment, File file, User author, Role authorRole, LocalDateTime submissionTime);

    Submission assessSubmission(Long submissionId, User grader, Float grade);
}
