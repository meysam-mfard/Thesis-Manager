package e.group.thesismanager.service;

import e.group.thesismanager.model.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AssessmentService {

    List<Thesis> getThesis();

    Document feedbackDocument(String comment, File file, User author, LocalDateTime submissionTime, Role authorRole);

    void assessDocument(Document document, Map<User,Float> grade, SubmissionType submissionType);
}
