package e.group.thesismanager.bootstrap;

import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.SemesterRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
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
    /*private final SubmissionRepository submissionRepository;
    private final DocumentRepository documentRepository;
    private final FeedbackRepository feedbackRepository;*/

    public DataBootstrap(UserRepository userRepository, ThesisRepository thesisRepository
            , SemesterRepository semesterRepository/*, SubmissionRepository submissionRepository
            , DocumentRepository documentRepository, FeedbackRepository feedbackRepository*/) {

        this.userRepository = userRepository;
        this.thesisRepository = thesisRepository;
        this.semesterRepository = semesterRepository;
        /*this.submissionRepository = submissionRepository;
        this.documentRepository = documentRepository;
        this.feedbackRepository = feedbackRepository;*/
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        loadUsers();
    }

    private void loadUsers() {

        /*Adding Users*/
        List<User> userList = new ArrayList<>();

        User std1 = new User("first1", "last1", "user1@lnu.se", "{noop}pass1", new HashSet<Role>(Arrays.asList(Role.ROLE_STUDENT, Role.ROLE_OPPONENT)));
        userList.add(std1);
        User std2 = new User("first2", "last2", "username2@lnu.se", "{noop}password2", new HashSet<Role>(Arrays.asList(Role.ROLE_STUDENT, Role.ROLE_OPPONENT)));
        userList.add(std2);
        User sup1 = new User("first-sup1", "last-sup1", "username-sup1@lnu.se", "{noop}password-sup1", new HashSet<Role>(Arrays.asList(Role.ROLE_SUPERVISOR, Role.ROLE_READER)));
        userList.add(sup1);
        User sup2 = new User("first-sup2", "last-sup2", "username-sup2@lnu.se", "{noop}password-sup2", new HashSet<Role>(Arrays.asList(Role.ROLE_SUPERVISOR, Role.ROLE_READER)));
        userList.add(sup2);
        User coo1 = new User("first-coo1", "last-coo1","username-coo1@lnu.se", "{noop}password-coo1", new HashSet<Role>(Arrays.asList(Role.ROLE_COORDINATOR)));
        coo1.getRoles().add(Role.ROLE_READER);
        userList.add(coo1);
        User admin = new User("admin1", "admin2", "admin@lnu.se", "{noop}admin", new HashSet<Role>(Arrays.asList(Role.ROLE_ADMIN)));
        userList.add(admin);

        log.info("Initializing users!");
        userRepository.saveAll(userList);
        //---------------------------------------------

        /*Adding Semester*/
        Semester semester = new Semester();
        semester.setActive(true);
        semester.setSemesterPeriod(SemesterPeriod.SPRING);
        semester.setYear(Year.of(2019));
        semester.setProjectDescriptionDeadline(LocalDateTime.of(2020, 1, 1, 23, 59));

        log.info("Initializing semesterPeriod!");
        semesterRepository.save(semester);
        //---------------------------------------------

        /*Adding Thesis*/
        Thesis t1 = new Thesis();
        t1.setSemester(semester);
        t1.setStudent(std1);
        t1.setCoordinator(coo1);
        t1.setSupervisor(sup1);
        t1.setSupervisorRequestStatus(SupervisorRequestStatus.REQUEST_SENT);
        t1.setOpponent(std2);
        //t1.getReaders().add(sup1);
        //t1.getBidders().add(sup1);

        //////////////////////////////
        //submission
        Submission sub_prjDsc = new Submission();
        sub_prjDsc.setType(SubmissionType.PROJECT_DESCRIPTION);
        Document doc1 = new Document();
        doc1.setAuthor(std1);
        doc1.setComment("This is my project description");

        //creating a Byte array file
        String str = "fake file content";
        byte[] file = new byte[str.getBytes().length];
        int i = 0;
        for (byte b : str.getBytes()){
            file[i++] = b;
        }
        doc1.setFile(file);
        doc1.setFileName("fileName.txt");
        doc1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        sub_prjDsc.setSubmittedDocument(doc1);

        //feedback
        Feedback feed1 = new Feedback();
        feed1.setAuthor(coo1);
        feed1.setAuthorRole(Role.ROLE_COORDINATOR);
        feed1.setComment("This is my feedback on your project description");

        //feedbackRepository.save(feed1);
        sub_prjDsc.getFeedbacks().add(feed1);
        sub_prjDsc.setGrade(12.5F);

        //submissionRepository.save(sub_prjDsc);
        t1.addSubmission(sub_prjDsc);
        /////////////////////////////////////

        //submission
        Submission sub_prjPlan = new Submission();
        sub_prjPlan.setType(SubmissionType.PROJECT_PLAN);
        Document doc2 = new Document();
        doc2.setAuthor(std1);
        doc2.setComment("This is my project description");
        doc2.setFile(file);
        doc2.setFileName("fileName2.txt");
        doc2.setFileType(MediaType.APPLICATION_PDF_VALUE);
        sub_prjPlan.setSubmittedDocument(doc2);

        //feedback
        Feedback feed2 = new Feedback();
        feed2.setAuthor(coo1);
        feed2.setAuthorRole(Role.ROLE_COORDINATOR);
        feed2.setComment("This is my feedback on your project description");

        //feedbackRepository.save(feed1);
        sub_prjPlan.getFeedbacks().add(feed2);
        sub_prjPlan.setGrade(28.5F);

        //submissionRepository.save(sub_prjDsc);
        t1.addSubmission(sub_prjPlan);
        /////////////////////////////////////

        //submission
        Submission sub_report = new Submission();
        sub_report.setType(SubmissionType.REPORT);
        Document doc3 = new Document();
        doc3.setAuthor(std1);
        doc3.setComment("This is my project description");
        doc3.setFile(file);
        doc3.setFileName("fileName2.txt");
        doc3.setFileType(MediaType.APPLICATION_PDF_VALUE);
        sub_report.setSubmittedDocument(doc3);

        //feedback
        Feedback feed3 = new Feedback();
        feed3.setAuthor(coo1);
        feed3.setAuthorRole(Role.ROLE_COORDINATOR);
        feed3.setComment("This is my feedback on your project description");

        //feedbackRepository.save(feed1);
        sub_report.getFeedbacks().add(feed3);
        sub_report.setGrade(28.5F);

        //submissionRepository.save(sub_prjDsc);
        t1.addSubmission(sub_report);
        /////////////////////////////////////

        //submission
        Submission sub_finalReport = new Submission();
        sub_finalReport.setType(SubmissionType.FINAL_REPORT);
        Document doc4 = new Document();
        doc4.setAuthor(std1);
        doc4.setComment("This is my project description");
        doc4.setFile(file);
        doc4.setFileName("fileName2.txt");
        doc4.setFileType(MediaType.APPLICATION_PDF_VALUE);
        sub_finalReport.setSubmittedDocument(doc4);

        //feedback
        Feedback feed4 = new Feedback();
        feed4.setAuthor(coo1);
        feed4.setAuthorRole(Role.ROLE_COORDINATOR);
        feed4.setComment("This is my feedback on your project description");

        //feedbackRepository.save(feed1);
        sub_finalReport.getFeedbacks().add(feed4);
        sub_finalReport.setGrade(28.5F);

        //submissionRepository.save(sub_prjDsc);
        t1.addSubmission(sub_finalReport);



        log.info("Initializing thesis!");
        thesisRepository.save(t1);

    }
}