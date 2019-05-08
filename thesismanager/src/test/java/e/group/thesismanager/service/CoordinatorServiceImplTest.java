package e.group.thesismanager.service;

import e.group.thesismanager.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CoordinatorServiceImplTest {
    CoordinatorService coordinatorService;
    User mockedStudent = new User("U1",
            "U11",
            new HashSet<>(Arrays.asList(Role.STUDENT)));
    private static final List<Thesis> THESIS_LIST = new LinkedList<>();
    @Mock
    AssessmentService assessmentService;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.initMocks(this);
        User student = new User("U1",
                "U11",
                new HashSet<>(Arrays.asList(Role.STUDENT)));
        Thesis thesis = new Thesis();
        thesis.setStudent(student);

        Submission s1 = new Submission();
        s1.setType(SubmissionType.PROJECT_PLAN);


        Submission s2 = new Submission();
        s2.setType(SubmissionType.FINAL_REPORT);


        thesis.addSubmission(s1);
        thesis.addSubmission(s2);

        THESIS_LIST.add(thesis);
        coordinatorService = new CoordinatorServiceImpl(assessmentService);
        coordinatorService.setThesis(THESIS_LIST);
    }

    @Test
    void InitiateThesisTest(){
        Thesis thesis = new Thesis();
        thesis.setStudent(mockedStudent);
        Thesis result = coordinatorService.initiateThesis(mockedStudent);
        assertEquals(thesis, result);
    }

   @Test
    void evaluateProjectPlanTest(){
       when(assessmentService.getThesis()).thenReturn(THESIS_LIST);
       Thesis mockedThesis = new Thesis();
       mockedThesis.setStudent(mockedStudent);
       Float grade = 1f;

       Thesis testThesis = coordinatorService.evaluateProjectPlan(mockedStudent, grade);
       Submission evaluatedSubmissions = testThesis.getSubmissions().get(0);

       assertEquals(SubmissionType.PROJECT_PLAN, evaluatedSubmissions.getType());
       assertEquals(grade, evaluatedSubmissions.getGrade());

   }

   @Test
    void gradeFinalProjectTest(){
       when(assessmentService.getThesis()).thenReturn(THESIS_LIST);
       Thesis mockedThesis = new Thesis();
       mockedThesis.setStudent(mockedStudent);
       Float grade = 1f;

       Thesis testThesis = coordinatorService.gradeFinalProject(mockedStudent, grade);
       Submission evaluatedFinal = testThesis.getSubmissions().get(1);

       assertEquals(SubmissionType.FINAL_REPORT, evaluatedFinal.getType());
       assertEquals(grade, evaluatedFinal.getGrade());
   }

   @Test
    void assignSupervisorTest(){
       when(assessmentService.getThesis()).thenReturn(THESIS_LIST);

       User supervisor = new User("s1",
               "s11",
               new HashSet<>(Arrays.asList(Role.SUPERVISOR)));


       Thesis testSupervisor = coordinatorService.assignSupervisor(mockedStudent, supervisor);

       assertEquals(supervisor, testSupervisor.getSupervisor());
   }
    @Test
    void assignOpponentTest(){
        when(assessmentService.getThesis()).thenReturn(THESIS_LIST);
        Set<User> mockedOpponent = new HashSet<>();
        User opponent1 = new User("01",
                "011",
                new HashSet<>(Arrays.asList(Role.SUPERVISOR)));
        mockedOpponent.add(opponent1);

        Thesis testOpponent = coordinatorService.assignOpponent(mockedStudent,mockedOpponent);
        assertEquals(mockedOpponent, testOpponent.getOpponent());
    }


}
