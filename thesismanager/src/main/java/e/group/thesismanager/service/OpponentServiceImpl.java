package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpponentServiceImpl extends FeedbackService implements OpponentService {

    @Autowired
    public OpponentServiceImpl(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                               SubmissionRepository submissionRepository, UserRepository userRepository) {

        super(thesisRepository, feedbackRepository, submissionRepository, userRepository);
    }

    @Override
    public Thesis getThesis(User user) {

        return thesisRepository.findThesisByOpponent(user).orElseThrow(() -> new NotFoundException("User not found."));
    }
}