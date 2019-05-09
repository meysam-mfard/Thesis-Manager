package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReaderServiceImpl extends AbstractService implements ReaderService {

    @Autowired
    public ReaderServiceImpl(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                         SubmissionRepository submissionRepository) {

        super(thesisRepository, feedbackRepository, submissionRepository);
    }

    @Override
    public Thesis bidOnThesis(Long thesisId, User reader) throws MissingRoleException {

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + thesisId));

        if(!reader.getRoles().contains(Role.READER))
            throw new MissingRoleException("Could not bid on thesis; User is not a reader");

        thesis.getBidders().add(reader);
        return thesisRepository.save(thesis);
    }
}