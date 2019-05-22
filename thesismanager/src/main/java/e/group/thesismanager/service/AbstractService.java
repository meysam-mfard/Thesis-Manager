package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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

    public Thesis getThesisById(Long id) {

        return thesisRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + id));
    }

    @Transactional
    public Submission feedbackOnSubmission(Long submissionId, Feedback feedback) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        submission.addFeedback(feedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission feedbackOnSubmission(Long submissionId, String comment, Byte[] file, User author,
                                           LocalDateTime submissionTime, Role authorRole) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        Feedback feedback = new Feedback(comment, file, author, submissionTime, authorRole);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        submission.addFeedback(savedFeedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission editFeedbackOnSubmission(Long submissionId, Long feedbackId, String updatedComment, Byte[] updatedFile, User updatedAuthor,
                                               LocalDateTime updatedSubmissionTime, Role updatedAuthorRole) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() ->
                new NotFoundException("Feedback does not exist. Id: " + feedbackId));

        int oldFeedbackIndex = submission.getFeedbacks().indexOf(feedback);

        if(updatedComment.isEmpty())
            updatedComment = feedback.getComment();

        if(updatedAuthor == null)
            updatedAuthor = feedback.getAuthor();

        feedback.setComment(updatedComment);
        feedback.setFile(updatedFile);
        feedback.setAuthor(updatedAuthor);
        feedback.setSubmissionTime(updatedSubmissionTime);
        feedback.setAuthorRole(updatedAuthorRole);

        feedbackRepository.save(feedback);
        submission.getFeedbacks().set(oldFeedbackIndex, feedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission assessSubmission(Long submissionId, Float grade) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        submission.setGrade(grade);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission editAssessmentSubmission(Long submissionId, Float updatedGrade) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        if(updatedGrade == null)
            updatedGrade = submission.getGrade();

        submission.setGrade(updatedGrade);
        return submissionRepository.save(submission);
    }
}