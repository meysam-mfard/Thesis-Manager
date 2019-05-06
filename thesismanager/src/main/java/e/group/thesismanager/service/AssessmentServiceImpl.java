package e.group.thesismanager.service;

import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentServiceImpl implements AssessmentService{

    protected ThesisRepository thesisRepository;

    protected FeedbackRepository feedbackRepository;

    protected SubmissionRepository submissionRepository;

    public AssessmentServiceImpl(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository, SubmissionRepository submissionRepository) {
        this.thesisRepository = thesisRepository;
        this.feedbackRepository = feedbackRepository;
        this.submissionRepository = submissionRepository;
    }

    @Autowired

    @Override
    public List<Thesis> getThesis() {
       return thesisRepository.findAll();
    }

    @Override
    public Document feedbackDocument(String comment, File file, User author, LocalDateTime submissionTime, Role authorRole) {
       //files, comment
        Feedback feedback = new Feedback();
        feedback.setComment(comment);
        feedback.setFile(file);
        feedback.setAuthor(author);
        feedback.setAuthorRole(authorRole);
        feedback.setSubmissionTime(submissionTime);
        return feedbackRepository.save(feedback);
    }

    // Grade
    @Override
    public void assessDocument(Document document, Map<User,Float> grade, SubmissionType submissionType) {
       Submission submission = new Submission();
       submission.setSubmittedDocument(document);
       submission.setType(submissionType);
       submission.setFeedbacks(feedbackRepository.findAll());
       submission.setGrades(grade);
      submissionRepository.save(submission);
    }
}
