package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssessmentServiceImpl implements AssessmentService{

    private final ThesisRepository thesisRepository;

    private final FeedbackRepository feedbackRepository;

    private final SubmissionRepository submissionRepository;

    public AssessmentServiceImpl(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                                 SubmissionRepository submissionRepository) {
        this.thesisRepository = thesisRepository;
        this.feedbackRepository = feedbackRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<Thesis> getThesis() {
        /*List<Thesis> thesisList = thesisRepository.findAll();
        thesisList.forEach(thesis -> {
            thesis.getSubmissions();
            thesis.setSubmissions(submissionRepository.findByThesisId(thesis.getId()));
        });
        return thesisList;*/
        return thesisRepository.findAll();
    }

    @Transactional
    @Override
    public Submission feedbackOnSubmission(Long submissionId, Feedback feedback) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: "+submissionId));

        submission.addFeedback(feedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    @Override
    public Submission feedbackOnSubmission(Long submissionId, String comment, File file, User author,
                                           Role authorRole, LocalDateTime submissionTime) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: "+submissionId));

        Feedback feedback = new Feedback();
        feedback.setComment(comment);
        feedback.setFile(file);
        feedback.setAuthor(author);
        feedback.setAuthorRole(authorRole);
        feedback.setSubmissionTime(submissionTime);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        submission.addFeedback(savedFeedback);

        return submissionRepository.save(submission);
    }

    @Transactional
    @Override
    public Submission assessSubmission(Long submissionId, Float grade) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: "+submissionId));

        submission.setGrade(grade);

        return submissionRepository.save(submission);
    }
}
