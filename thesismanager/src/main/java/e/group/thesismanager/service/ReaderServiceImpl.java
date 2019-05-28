package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    private ThesisRepository thesisRepository;

    @Autowired
    public ReaderServiceImpl(ThesisRepository thesisRepository) {

        this.thesisRepository = thesisRepository;
    }

    @Override
    public List<Thesis> getPossibleTheses(User user) {

        List<Thesis> allTheses = thesisRepository.findAll();
        List<Thesis> possibleTheses = new ArrayList<Thesis>();

        for(Thesis thesis: allTheses) {

            if(!thesis.getReaders().contains(user) && !thesis.getBidders().contains(user)) {

                possibleTheses.add(thesis);
            }
        }

        return possibleTheses;
    }

    @Override
    public  List<Thesis> getAssignedTheses(User user) {

        return thesisRepository.findThesesByReaders(user);
    }

    @Override
    public Thesis bidOnThesis(Long thesisId, User reader) throws MissingRoleException {

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + thesisId));

        if(!reader.getRoles().contains(Role.ROLE_READER))
            throw new MissingRoleException("Could not bid on thesis; User is not a reader");

        thesis.getBidders().add(reader);
        return thesisRepository.save(thesis);
    }
}