package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SupervisorServiceImplTest {

    SupervisorService supervisorService;
    User mockedSupervisor = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.SUPERVISOR)));
    private static final List<Thesis> THESIS_LIST = new LinkedList<>();

    @Mock
    ThesisRepository thesisRepository;
    @Mock
    FeedbackRepository feedbackRepository;
    @Mock
    SubmissionRepository submissionRepository;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.initMocks(this);
        supervisorService = new SupervisorServiceImpl(thesisRepository, feedbackRepository, submissionRepository);
        User supervisor = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.SUPERVISOR)));
        Thesis thesis = new Thesis();
        thesis.setStudent(new User());
        thesis.setSemester(new Semester());

        thesis.setSupervisor(supervisor);
        thesis.setSupervisorAccept(true);

        THESIS_LIST.add(thesis);
    }

    @Test
    void replyOnSupervisionPropositionTest() {

        assertThrows(NotFoundException.class, () -> supervisorService.replyOnSupervisionProposition(THESIS_LIST.get(0).getId(), mockedSupervisor, true));
    }
}