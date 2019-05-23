package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public abstract class AbstractService {

    protected ThesisRepository thesisRepository;
    protected FeedbackRepository feedbackRepository;
    protected SubmissionRepository submissionRepository;
    protected UserRepository userRepository;

    public AbstractService(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                                 SubmissionRepository submissionRepository, UserRepository userRepository) {

        this.thesisRepository = thesisRepository;
        this.feedbackRepository = feedbackRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
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

    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User does not exist. Username: " + username));
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
    public Submission feedbackOnSubmission(Long submissionId, String comment, byte[] file, String fileName, String fileType, User author,
                                           LocalDateTime submissionTime, Role authorRole) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        Feedback feedback = new Feedback(comment, file, fileName, fileType, author, submissionTime, authorRole);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        submission.addFeedback(savedFeedback);
        return submissionRepository.save(submission);
    }

    public Submission feedbackCommentOnSubmission(Long submissionId, String comment, Long authorId, Role authorRole) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("User not found. ID: "+authorId));
        Optional<Feedback> feedbackOptional = submission.getFeedbacks().stream().filter(feedback -> feedback.getAuthor().equals(author))
                .findFirst();
        Feedback feedback;
        if (!feedbackOptional.isPresent())
            feedback = new Feedback(comment, null, null, null, author, null, authorRole);
        else {
            feedback = feedbackOptional.get();
            feedback.setComment(comment);
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);
        submission.addFeedback(savedFeedback);
        return submissionRepository.save(submission);
    }

    public Submission feedbackFileOnSubmission(Long submissionId, MultipartFile multipartFile, Long authorId, Role authorRole) throws IOException {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("User not found. ID: "+authorId));
        Optional<Feedback> feedbackOptional = submission.getFeedbacks().stream().filter(feedback -> feedback.getAuthor().equals(author))
                .findFirst();

        byte[] file = new byte[multipartFile.getBytes().length];
        int i = 0;
        for (byte b : multipartFile.getBytes()){
            file[i++] = b;
        }

        Feedback feedback;
        if (!feedbackOptional.isPresent())
            feedback = new Feedback(null, file, multipartFile.getName(), multipartFile.getContentType()
                    , author, null, authorRole);
        else {
            feedback = feedbackOptional.get();
            feedback.setFile(file);
            feedback.setFileName(multipartFile.getName());
            feedback.setFileType(multipartFile.getContentType());
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);
        submission.addFeedback(savedFeedback);
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission editFeedbackOnSubmission(Long submissionId, Long feedbackId, String updatedComment, byte[] updatedFile, String fileName, String fileType, User updatedAuthor,
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
        feedback.setFileName(fileName);
        feedback.setFileType(fileType);
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