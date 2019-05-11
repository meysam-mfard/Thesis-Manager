package e.group.thesismanager.bootstrap;

import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@Profile("default")
public class DataBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ThesisRepository thesisRepository;
    private final SemesterRepository semesterRepository;
    private final SubmissionRepository submissionRepository;
    private final DocumentRepository documentRepository;
    private final FeedbackRepository feedbackRepository;

    public DataBootstrap(UserRepository userRepository, ThesisRepository thesisRepository
            , SemesterRepository semesterRepository, SubmissionRepository submissionRepository, DocumentRepository documentRepository, FeedbackRepository feedbackRepository) {

        this.userRepository = userRepository;
        this.thesisRepository = thesisRepository;
        this.semesterRepository = semesterRepository;
        this.submissionRepository = submissionRepository;
        this.documentRepository = documentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        loadUsers();
    }

    private void loadUsers() {

        /*Adding Users*/
        List<User> userList = new ArrayList<>();

        User std1 = new User("first1", "last1", "user1", "{noop}pass1", new HashSet<Role>(Arrays.asList(Role.STUDENT, Role.OPPONENT)));
        userList.add(std1);
        User std2 = new User("first2", "last2", "username2", "{noop}password2", new HashSet<Role>(Arrays.asList(Role.STUDENT)));
        userList.add(std2);
        User sup1 = new User("first-sup1", "last-sup1", "username-sup1", "{noop}password-sup1", new HashSet<Role>(Arrays.asList(Role.SUPERVISOR, Role.READER)));
        userList.add(sup1);
        User sup2 = new User("first-sup2", "last-sup2", "username-sup2", "{noop}password-sup2", new HashSet<Role>(Arrays.asList(Role.SUPERVISOR, Role.READER)));
        userList.add(sup2);
        User coo1 = new User("first-coo1", "last-coo1","username-coo1", "{noop}password-coo1", new HashSet<Role>(Arrays.asList(Role.COORDINATOR)));
        coo1.getRoles().add(Role.READER);
        userList.add(coo1);

        log.info("Initializing users!");
        userRepository.saveAll(userList);
        //---------------------------------------------

        /*Adding Semester*/
        Semester semester = new Semester();
        semester.setSemester(SemesterAT_SP.SPRING);
        semester.setYear(Year.of(2019));
        semester.setProjectDescriptionDeadline(LocalDateTime.of(2020, 1, 1, 23, 59));

        log.info("Initializing semester!");
        semesterRepository.save(semester);
        //---------------------------------------------

        /*Adding Thesis*/
        Thesis t1 = new Thesis();
        t1.setSemester(semester);
        t1.setStudent(std1);
        t1.setCoordinator(coo1);
        t1.setSupervisor(sup1);

        //submission
        Submission sub_prjDsc = new Submission();
        sub_prjDsc.setType(SubmissionType.PROJECT_DESCRIPTION);
        Document doc1 = new Document();
        doc1.setAuthor(std1);
        doc1.setComment("This is my project description");
        
        //documentRepository.save(doc1);
        sub_prjDsc.setSubmittedDocument(doc1);

        //feedback
        Feedback feed1 = new Feedback();
        feed1.setAuthor(coo1);
        feed1.setAuthorRole(Role.COORDINATOR);
        feed1.setComment("This is my feedback on your project description");

        //feedbackRepository.save(feed1);
        sub_prjDsc.getFeedbacks().add(feed1);
        sub_prjDsc.setGrade(12.5F);

        //submissionRepository.save(sub_prjDsc);
        t1.addSubmission(sub_prjDsc);

        log.info("Initializing thesis!");
        thesisRepository.save(t1);
    }
}