package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public abstract class AbstractService {

    protected ThesisRepository thesisRepository;
    protected FeedbackRepository feedbackRepository;
    protected SubmissionRepository submissionRepository;

    public AbstractService(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                                 SubmissionRepository submissionRepository) {

        this.thesisRepository = thesisRepository;
        this.feedbackRepository = feedbackRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<Thesis> getThesis() {

        /*
        List<Thesis> thesisList = thesisRepository.findAll();
        thesisList.forEach(thesis -> {
            thesis.getSubmissions();
            thesis.setSubmissions(submissionRepository.findThesisById(thesis.getId()));
        });
        return thesisList;
        */

        return thesisRepository.findAll();
    }

    @Transactional
    public Submission feedbackOnSubmission(Long submissionId, Feedback feedback) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        submission.addFeedback(feedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission feedbackOnSubmission(Long submissionId, String comment, File file, User author,
                                           LocalDateTime submissionTime, Role authorRole) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        Feedback feedback = new Feedback(comment, file, author, submissionTime, authorRole);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        submission.addFeedback(savedFeedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission assessSubmission(Long submissionId, Float grade) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        submission.setGrade(grade);
        return submissionRepository.save(submission);
    }
}