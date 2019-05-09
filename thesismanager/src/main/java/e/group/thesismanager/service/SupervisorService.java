package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface SupervisorService {

    List<Thesis> getThesis();

    Submission feedbackOnSubmission(Long submissionId, Feedback feedback);

    Submission feedbackOnSubmission(Long submissionId, String comment, File file, User author, LocalDateTime submissionTime, Role authorRole);

    Submission assessSubmission(Long submissionId, Float grade);

    Thesis replyOnSupervisionProposition (Long thesisId, User supervisor, boolean answer) throws MissingRoleException;
}