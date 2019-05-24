package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    protected ThesisRepository thesisRepository;
    protected FeedbackRepository feedbackRepository;
    protected SubmissionRepository submissionRepository;
    protected UserRepository userRepository;

    public FeedbackService(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                           SubmissionRepository submissionRepository, UserRepository userRepository) {

        this.thesisRepository = thesisRepository;
        this.feedbackRepository = feedbackRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    public List<Thesis> getThesis() {
        return thesisRepository.findAll();
    }

    public Submission getSubmissionById(Long submissionId) {
        return submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exists. ID: "+ submissionId));
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
    public Submission feedbackOnSubmission(Long submissionId, String comment, MultipartFile multipartFile
            , Long authorId, Role authorRole) throws IOException, MissingRoleException {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundException("User not found. ID: "+authorId));
        Optional<Feedback> feedbackOptional = submission.getFeedbacks().stream().filter(feedback -> feedback.getAuthor().equals(author))
                .findFirst();

        if (!author.getRoles().contains(authorRole))
            throw new MissingRoleException("User does not have the specified role. ID: " + authorId + ", role: " + authorRole);

        byte[] file = null;
        if(!multipartFile.isEmpty()) {
            file = new byte[multipartFile.getBytes().length];
            int i = 0;
            for (byte b : multipartFile.getBytes()){
                file[i++] = b;
            }
        }
        Feedback feedback;
        if (!feedbackOptional.isPresent()) {
            feedback = new Feedback(comment, file, multipartFile.getOriginalFilename(), multipartFile.getContentType()
                    , author, null, authorRole);
            submission.addFeedback(feedback);
        }
        else {
            feedback = feedbackOptional.get();
            feedback.setAuthor(author);
            feedback.setAuthorRole(authorRole);
            feedback.setComment(comment);
            feedback.setFile(file);
            feedback.setFileName(multipartFile.getOriginalFilename());
            feedback.setFileType(multipartFile.getContentType());
        }

        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission assessSubmission(Long submissionId, Float grade) {

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() ->
                new NotFoundException("Submission does not exist. Id: " + submissionId));

        submission.setGrade(grade);

        if (submission.getType().equals(SubmissionType.FINAL_REPORT)) {
            Thesis thesis = submission.getThesis();
            thesis.setFinalGrade(grade);
            thesisRepository.save(thesis);
        }

        return submissionRepository.save(submission);
    }
}