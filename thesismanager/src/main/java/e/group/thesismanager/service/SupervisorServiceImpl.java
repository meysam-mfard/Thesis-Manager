package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    private ThesisRepository thesisRepository;

    @Autowired
    public SupervisorServiceImpl(ThesisRepository thesisRepository) {

        this.thesisRepository = thesisRepository;
    }

    @Override
    public Thesis replyOnSupervisionProposition(Long thesisId, User supervisor, SupervisorRequestStatus answer) throws MissingRoleException {

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + thesisId));

        if(!supervisor.getRoles().contains(Role.ROLE_SUPERVISOR))
            throw new MissingRoleException("Could not reply on supervision proposition; User is not a supervisor");

        thesis.setSupervisor(supervisor);
        thesis.setSupervisorRequestStatus(answer);
        return thesisRepository.save(thesis);
    }

    @Override
    public List<Thesis> getThesis(User user) {

        return thesisRepository.findThesesBySupervisorAndSupervisorRequestStatus(user, SupervisorRequestStatus.ACCEPTED);
    }

    @Override
    public List<Thesis> getRequests(User user) {

        return thesisRepository.findThesesBySupervisorAndSupervisorRequestStatus(user, SupervisorRequestStatus.REQUEST_SENT);
    }
}