package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupervisorServiceIT {

    @Autowired
    SupervisorService supervisorService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThesisRepository thesisRepository;

    @Transactional
    @Test
    public void replyOnSupervisionPropositionTest() throws MissingRoleException {

        supervisorService.replyOnSupervisionProposition(thesisRepository.findById(1L).get().getId(), userRepository.findById(3L).get(), SupervisorRequestStatus.ACCEPTED);

        assertEquals(userRepository.findById(3L).get(), supervisorService.getThesis().get(0).getSupervisor());
    }
}