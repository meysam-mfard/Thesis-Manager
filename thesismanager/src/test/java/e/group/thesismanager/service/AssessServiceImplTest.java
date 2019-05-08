package e.group.thesismanager.service;

import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AssessServiceImplTest {
    AssessmentService assessmentService;
    private static final List<Thesis> THESIS_LIST = new LinkedList<>();
    private static final String FN_1 = "fn1";
    private static final String LN_1 = "ln1";


    @Mock
    ThesisRepository thesisRepository;

    @Mock
    FeedbackRepository feedbackRepository;

    @Mock
    SubmissionRepository submissionRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        assessmentService = new AssessmentServiceImpl(thesisRepository, feedbackRepository, submissionRepository);
        User user1= new User(FN_1, LN_1, new HashSet<>(Arrays.asList(Role.STUDENT)));
        User user2 = new User(FN_1, LN_1, new HashSet<>(Arrays.asList(Role.STUDENT)));
        User user3 = new User(FN_1, LN_1, new HashSet<>(Arrays.asList(Role.STUDENT)));
        Semester semester = new Semester();
        Set<User> readers = new HashSet<>();

        Set<User> bidders = new HashSet<>();

        Set<User> opponent = new HashSet<>();

        List<Submission> submissions = new LinkedList<>();

        Float finalGrade=34f;

        Thesis thesis1 = new Thesis();
        thesis1.setCoordinator(user1);
        thesis1.setSemester(semester);
        thesis1.setStudent(user2);
        thesis1.setSupervisor(user3);
        thesis1.setBidders(bidders);
        thesis1.setOpponent(opponent);
        thesis1.setReaders(readers);
        thesis1.setSubmissions(submissions);
        thesis1.setFinalGrade(finalGrade);

        THESIS_LIST.add(thesis1);
    }

    @Test
    void getThesis() {
        when(thesisRepository.findAll()).thenReturn(null);
        assertNull(assessmentService.getThesis());

        when(thesisRepository.findAll()).thenReturn(THESIS_LIST);
        assertEquals(1, assessmentService.getThesis().size());
        assertArrayEquals(THESIS_LIST.toArray(), assessmentService.getThesis().toArray());
    }

    @Test
    void feedbackOnSubmission() {
        Feedback feedback = new Feedback();
        String comment="good";
        File file = new File("");
        User author = new User(FN_1, "K", new HashSet<>(Arrays.asList(Role.STUDENT)));
        Role authorRole = Role.STUDENT;
        LocalDateTime submissionTime= LocalDateTime.now();
        feedback.setComment(comment);
        feedback.setFile(file);
        feedback.setAuthor(author);
        feedback.setAuthorRole(authorRole);
        feedback.setSubmissionTime(submissionTime);
        Submission submission = new Submission();
        submission.setThesis(THESIS_LIST.get(0));
        submission.setSubmittedDocument(new Document());
        submission.setType(SubmissionType.PROJECT_DESCRIPTION);
        submission.getFeedbacks().add(feedback);

        when(feedbackRepository.save(any())).thenReturn(feedback);
        when(submissionRepository.save(any())).thenReturn(submission);
        when(submissionRepository.findById(any())).thenReturn(Optional.of(submission));
        assertEquals(submission, assessmentService.feedbackOnSubmission( THESIS_LIST.get(0).getId(),comment,file,author,authorRole,submissionTime));
    }

    @Test
    void assessDocument() {
        Submission submission = new Submission();
        Document document = new Document();
        String comment="good";
        File file = new File("");
        User author = new User(FN_1, "K", new HashSet<>(Arrays.asList(Role.STUDENT)));
        LocalDateTime submissionTime= LocalDateTime.now();

        document.setAuthor(author);
        document.setComment(comment);
        document.setFile(file);
        document.setSubmissionTime(submissionTime);

        SubmissionType submissionType=SubmissionType.PROJECT_DESCRIPTION;
        Map<User,Float> grades = new HashMap<>();

        submission.setSubmittedDocument(document);
        submission.setType(submissionType);
        submission.setFeedbacks(feedbackRepository.findAll());
        submission.setGrades(grades);

        when(submissionRepository.findById(any())).thenReturn(Optional.of(submission));
        when(submissionRepository.save(submission)).thenReturn(submission);
        assessmentService.assessSubmission(submission.getId(), new User(), 1F);
        assertEquals(submission.getSubmittedDocument(),document);
        assertEquals(submission.getType(),submissionType);
        assertEquals(submission.getGrades(), grades);
        //assertEquals(submission);
        // assessmentService.assessSubmission(document,grade,submissionType);

        // verify(assessmentService,times(3)).assessSubmission(document,grade,submissionType);

    }
}
