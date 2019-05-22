package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
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
import static org.mockito.Mockito.when;

public class CoordinatorServiceImplTest {

    CoordinatorService coordinatorService;
    User mockedStudent = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.ROLE_STUDENT)));
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
        User student = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.ROLE_STUDENT)));
        Thesis thesis = new Thesis();
        thesis.setStudent(student);

        Submission s1 = new Submission();
        s1.setType(SubmissionType.PROJECT_PLAN);

        Submission s2 = new Submission();
        s2.setType(SubmissionType.FINAL_REPORT);

        thesis.addSubmission(s1);
        thesis.addSubmission(s2);

        THESIS_LIST.add(thesis);
        coordinatorService = new CoordinatorServiceImpl(thesisRepository, feedbackRepository, submissionRepository, userRepository);
        coordinatorService.setThesis(THESIS_LIST);
    }

    @Test
    void InitiateThesisTest() throws MissingRoleException {

        Thesis thesis = new Thesis();
        thesis.setStudent(mockedStudent);
        thesis.setSemester(new Semester());
        Thesis result = coordinatorService.initiateThesis(mockedStudent);
        assertEquals(thesis, result);
    }

    @Test
    void evaluateProjectPlanTest() throws MissingRoleException {

        when(coordinatorService.getThesis()).thenReturn(THESIS_LIST);
        Thesis mockedThesis = new Thesis();
        mockedThesis.setStudent(mockedStudent);
        Float grade = 1f;

        Thesis testThesis = coordinatorService.evaluateProjectPlan(mockedStudent, grade);
        Submission evaluatedSubmissions = testThesis.getSubmissions().get(0);

        assertEquals(SubmissionType.PROJECT_PLAN, evaluatedSubmissions.getType());
        assertEquals(grade, evaluatedSubmissions.getGrade());
    }

    @Test
    void gradeFinalProjectTest() throws MissingRoleException {

        when(coordinatorService.getThesis()).thenReturn(THESIS_LIST);
        Thesis mockedThesis = new Thesis();
        mockedThesis.setStudent(mockedStudent);
        Float grade = 1f;

        Thesis testThesis = coordinatorService.gradeFinalProject(mockedStudent, grade);
        Submission evaluatedFinal = testThesis.getSubmissions().get(1);

        assertEquals(SubmissionType.FINAL_REPORT, evaluatedFinal.getType());
        assertEquals(grade, evaluatedFinal.getGrade());
    }

    @Test
    void assignSupervisorTest() throws MissingRoleException {

        when(coordinatorService.getThesis()).thenReturn(THESIS_LIST);

        User supervisor = new User("s1", "s11", "s111", "s1111", new HashSet<>(Arrays.asList(Role.ROLE_SUPERVISOR)));


        Thesis testSupervisor = coordinatorService.assignSupervisor(mockedStudent, supervisor);

        assertEquals(supervisor, testSupervisor.getSupervisor());
    }
    @Test
    void assignOpponentTest() throws MissingRoleException {

        when(coordinatorService.getThesis()).thenReturn(THESIS_LIST);
        User mockedOpponent = new User("01", "011", "0111", "01111", new HashSet<>(Arrays.asList(Role.ROLE_SUPERVISOR)));

        Thesis testOpponent = coordinatorService.assignOpponent(mockedStudent,mockedOpponent);
        assertEquals(mockedOpponent, testOpponent.getOpponent());
    }
}
