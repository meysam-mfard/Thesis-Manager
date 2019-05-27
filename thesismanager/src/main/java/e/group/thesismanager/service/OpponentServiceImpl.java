package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpponentServiceImpl implements OpponentService {

    private ThesisRepository thesisRepository;

    @Autowired
    public OpponentServiceImpl(ThesisRepository thesisRepository) {

        this.thesisRepository = thesisRepository;
    }

    @Override
    public Thesis getThesis(User user) {

        return thesisRepository.findThesisByOpponent(user).orElseThrow(() -> new NotFoundException("User not found."));
    }
}