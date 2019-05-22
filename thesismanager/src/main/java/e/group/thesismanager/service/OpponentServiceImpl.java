package e.group.thesismanager.service;

import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpponentServiceImpl extends AbstractService implements OpponentService {

    @Autowired
    public OpponentServiceImpl(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                             SubmissionRepository submissionRepository) {

        super(thesisRepository, feedbackRepository, submissionRepository);
    }

    @Override
    public List<Thesis> getThesis(User user) {

        return thesisRepository.findThesesByOpponent(user);
    }
}