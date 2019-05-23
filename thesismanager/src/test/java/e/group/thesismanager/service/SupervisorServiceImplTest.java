package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SupervisorServiceImplTest {

    SupervisorService supervisorService;
    User mockedSupervisor = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.ROLE_SUPERVISOR)));
    private static final List<Thesis> THESIS_LIST = new LinkedList<>();

    @Mock
    ThesisRepository thesisRepository;
    @Mock
    FeedbackRepository feedbackRepository;
    @Mock
    SubmissionRepository submissionRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.initMocks(this);
        supervisorService = new SupervisorServiceImpl(thesisRepository, feedbackRepository, submissionRepository, userRepository);
        User supervisor = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.ROLE_SUPERVISOR)));
        Thesis thesis = new Thesis();
        thesis.setStudent(new User());
        thesis.setSemester(new Semester());

        thesis.setSupervisor(supervisor);
        thesis.setSupervisorRequestStatus(SupervisorRequestStatus.ACCEPTED);

        THESIS_LIST.add(thesis);
    }

    @Test
    void replyOnSupervisionPropositionErrorTest() {

        assertThrows(NotFoundException.class, () -> supervisorService.replyOnSupervisionProposition(THESIS_LIST.get(0).getId(), mockedSupervisor, SupervisorRequestStatus.ACCEPTED));
    }

    @Test
    void replyOnSupervisionPropositionSuccessTest() throws MissingRoleException {

        Thesis result = new Thesis();
        when(thesisRepository.findById(result.getId())).thenReturn(Optional.of(result));
        supervisorService.replyOnSupervisionProposition(result.getId(), mockedSupervisor, SupervisorRequestStatus.ACCEPTED);
        result.setStudent(THESIS_LIST.get(0).getStudent());
        result.setSemester(THESIS_LIST.get(0).getSemester());

        assertEquals(THESIS_LIST.get(0), result);
    }
}