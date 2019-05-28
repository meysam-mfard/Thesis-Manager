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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@Profile("default")
public class DataBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ThesisRepository thesisRepository;
    private final SemesterRepository semesterRepository;

    public DataBootstrap(UserRepository userRepository,
                         ThesisRepository thesisRepository,
                         SemesterRepository semesterRepository) {

        this.userRepository = userRepository;
        this.thesisRepository = thesisRepository;
        this.semesterRepository = semesterRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createData();
    }

    private void createData() {
        // Roles
        Set<Role> roleAdmin = new HashSet<>(Arrays.asList(Role.ROLE_ADMIN));
        Set<Role> rolesStudentOpponent = new HashSet<>(Arrays.asList(Role.ROLE_STUDENT, Role.ROLE_OPPONENT));
        Set<Role> rolesStudentOpponentReader = new HashSet<>(Arrays.asList(Role.ROLE_STUDENT, Role.ROLE_OPPONENT, Role.ROLE_READER));
        Set<Role> roleCoordinator = new HashSet<>(Arrays.asList(Role.ROLE_COORDINATOR));
        Set<Role> roleSupervisor = new HashSet<>(Arrays.asList(Role.ROLE_SUPERVISOR));
        Set<Role> roleSupervisorReader = new HashSet<>(Arrays.asList(Role.ROLE_SUPERVISOR, Role.ROLE_READER));
        Set<Role> roleReader = new HashSet<>(Arrays.asList(Role.ROLE_READER));

        // Users
        List<User> users = new ArrayList<>();

        // Admin
        User userAdmin1 = new User("admin", "admin", "admin", "{noop}admin", roleAdmin);
        users.add(userAdmin1);

        // Student + Opponent
        User userStuopp1 = new User("Robin", "Larsson", "roblar@lnu.se", "{noop}roblar", rolesStudentOpponent);
        users.add(userStuopp1);
        User userStuopp2 = new User("Adell", "Tatrous", "adetat@lnu.se", "{noop}adetat", rolesStudentOpponent);
        users.add(userStuopp2);
        User userStuopp3 = new User("Angelo", "Spadea", "angspa@lnu.se", "{noop}angspa", rolesStudentOpponent);
        users.add(userStuopp3);
        User userStuopp4 = new User("Helena", "Tevar", "heltev@lnu.se", "{noop}heltev", rolesStudentOpponent);
        users.add(userStuopp4);

        // Student + Opponent + Reader
        User userStuopprea1 = new User("Jiahui", "Le", "jiale@lnu.se", "{noop}jiale", rolesStudentOpponentReader);
        users.add(userStuopprea1);
        User userStuopprea2 = new User("Yinlong", "Yao", "yinyao@lnu.se", "{noop}yinyao", rolesStudentOpponentReader);
        users.add(userStuopprea2);
        User userStuopprea3 = new User("Meysam", "Fard", "meyfar@lnu.se", "{noop}meyfar", rolesStudentOpponentReader);
        users.add(userStuopprea3);
        User userStuopprea4 = new User("Jonas", "Altberg", "basshunter@ventrilo.com", "{noop}basshunter", rolesStudentOpponentReader);
        users.add(userStuopprea4);

        // Coordinator
        User userCoo1 = new User("Erik", "Andersson", "erk@lnu.se", "{noop}erk", roleCoordinator);
        users.add(userCoo1);
        User userCoo2 = new User("Daniel", "Larsson", "dala@lnu.se", "{noop}dala", roleCoordinator);
        users.add(userCoo2);

        // Supervisor
        User userSup1 = new User("Gun", "Nilsson", "gunsan@lnu.se", "{noop}gunsan", roleSupervisor);
        users.add(userSup1);

        // Supervisor + Reader
        User userSupRea1 = new User("Nils", "Karlssom", "nilkar@lnu.se", "{noop}nilkar", roleSupervisorReader);
        users.add(userSupRea1);
        User userSupRea2 = new User("Amanda", "Smith", "amasmi@lnu.se", "{noop}amasmi", roleSupervisorReader);
        users.add(userSupRea2);

        // Reader
        User userRea1 = new User("Christoffer", "Nordstrom", "hylve@lan.se", "{noop}hylve", roleReader);
        users.add(userRea1);
        User userRea2 = new User("Yume", "Hoashi", "yume@tarou.com", "{noop}yume", roleReader);
        users.add(userRea2);

        userRepository.saveAll(users);

        // Semesters
        Semester sem1 = new Semester();
        sem1.setYear(Year.of(2018));
        sem1.setSemesterPeriod(SemesterPeriod.AUTUMN);
        sem1.setActive(false);
        semesterRepository.save(sem1);

        Semester sem2 = new Semester();
        sem2.setYear(Year.of(2019));
        sem2.setSemesterPeriod(SemesterPeriod.SPRING);
        sem2.setActive(true);
        sem2.setProjectDescriptionDeadline(LocalDateTime.of(2019, 4, 26, 23, 50));
        sem2.setProjectPlanDeadline(LocalDateTime.of(2019, 6, 20, 23, 50));
        sem2.setReportDeadline(LocalDateTime.of(2019, 7, 20, 23, 50));
        sem2.setFinalReportDeadline(LocalDateTime.of(2019, 8, 10, 23, 50));
        semesterRepository.save(sem2);

        // Files
        String str1 = "No one knows";
        byte[] file1 = new byte[str1.getBytes().length];
        int i = 0;
        for (byte b : str1.getBytes()){
            file1[i++] = b;
        }

        // Thesis 1
        Document doc1_1 = new Document();
        doc1_1.setAuthor(userStuopp1);
        doc1_1.setSubmissionTime(LocalDateTime.of(2019, 3, 18, 12, 45));
        doc1_1.setFile(file1);
        doc1_1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc1_1.setFileName("What happened to Lunarstorm? - Project description");

        Document doc1_2 = new Document();
        doc1_2.setAuthor(userStuopp1);
        doc1_2.setSubmissionTime(LocalDateTime.of(2019, 3, 30, 12, 22));
        doc1_2.setFile(file1);
        doc1_2.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc1_2.setFileName("What happened to Lunarstorm? - Project plan");

        Document doc1_3 = new Document();
        doc1_3.setAuthor(userStuopp1);
        doc1_3.setSubmissionTime(LocalDateTime.of(2019, 4, 22, 12, 22));
        doc1_3.setFile(file1);
        doc1_3.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc1_3.setFileName("What happened to Lunarstorm? - Report");

        Feedback fb1_1 = new Feedback();
        fb1_1.setComment("Looks good");
        fb1_1.setAuthor(userCoo1);
        fb1_1.setAuthorRole(Role.ROLE_COORDINATOR);
        fb1_1.setSubmissionTime(LocalDateTime.of(2019, 3, 22, 15, 45));

        Feedback fb1_2 = new Feedback();
        fb1_2.setComment("Please find out what happened to Kamrat next");
        fb1_2.setAuthor(userSupRea2);
        fb1_2.setAuthorRole(Role.ROLE_SUPERVISOR);
        fb1_2.setSubmissionTime(LocalDateTime.of(2019, 4, 4, 9, 1));

        Submission sub1_1 = new Submission();
        sub1_1.setType(SubmissionType.PROJECT_DESCRIPTION);
        sub1_1.setSubmittedDocument(doc1_1);
        sub1_1.setFeedbacks(Arrays.asList(fb1_1));
        sub1_1.setGrade(1f);

        Submission sub1_2 = new Submission();
        sub1_2.setType(SubmissionType.PROJECT_PLAN);
        sub1_2.setSubmittedDocument(doc1_2);
        sub1_2.setFeedbacks(Arrays.asList(fb1_2));
        sub1_2.setGrade(1f);

        Submission sub1_3 = new Submission();
        sub1_3.setType(SubmissionType.REPORT);
        sub1_3.setSubmittedDocument(doc1_3);

        Thesis thesis1 = new Thesis();
        thesis1.setCoordinator(userCoo1);
        thesis1.setSupervisor(userSupRea2);
        thesis1.setSupervisorRequestStatus(SupervisorRequestStatus.ACCEPTED);
        thesis1.setStudent(userStuopp1);
        thesis1.setReaders(Stream.of(userRea1, userStuopprea1, userSupRea1).collect(Collectors.toSet()));
        thesis1.setOpponent(userStuopprea4);
        thesis1.setSemester(sem2);
        thesis1.setSubmissions(Arrays.asList(sub1_1, sub1_2, sub1_3));
        sub1_1.setThesis(thesis1);
        sub1_2.setThesis(thesis1);
        sub1_3.setThesis(thesis1);
        thesisRepository.save(thesis1);

        // Thesis 2

        Document doc2_1 = new Document();
        doc2_1.setFileName("The origin of gravel");
        doc2_1.setSubmissionTime(LocalDateTime.of(2019, 3, 3, 22, 0));
        doc2_1.setFile(file1);
        doc2_1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc2_1.setAuthor(userStuopp4);

        Feedback fb2_1 = new Feedback();
        fb2_1.setAuthor(userCoo2);
        fb2_1.setAuthorRole(Role.ROLE_COORDINATOR);
        fb2_1.setSubmissionTime(LocalDateTime.of(2019, 3, 4, 10, 0));
        fb2_1.setComment("I want my 5 minutes back");

        Submission sub2_1 = new Submission();
        sub2_1.setType(SubmissionType.PROJECT_DESCRIPTION);
        sub2_1.setSubmittedDocument(doc2_1);
        sub2_1.setFeedbacks(Arrays.asList(fb2_1));
        sub2_1.setGrade(0f);

        Thesis thesis2 = new Thesis();
        thesis2.setStudent(userStuopp4);
        thesis2.setCoordinator(userCoo2);
        thesis2.setSemester(sem2);
        thesis2.setSubmissions(Arrays.asList(sub2_1));
        sub2_1.setThesis(thesis2);
        thesisRepository.save(thesis2);

        // Thesis 3
        Document doc3_1 = new Document();
        doc3_1.setAuthor(userStuopp4);
        doc3_1.setSubmissionTime(LocalDateTime.of(2019, 3, 19, 12, 45));
        doc3_1.setFile(file1);
        doc3_1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc3_1.setFileName("The future of online communication - Project description");

        Document doc3_2 = new Document();
        doc3_2.setAuthor(userStuopp4);
        doc3_2.setSubmissionTime(LocalDateTime.of(2019, 3, 25, 12, 22));
        doc3_2.setFile(file1);
        doc3_2.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc3_2.setFileName("The future of online communication - Project plan");

        Document doc3_3 = new Document();
        doc3_3.setAuthor(userStuopp4);
        doc3_3.setSubmissionTime(LocalDateTime.of(2019, 4, 20, 18, 2));
        doc3_3.setFile(file1);
        doc3_3.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc3_3.setFileName("The future of online communication - Report");

        Feedback fb3_1 = new Feedback();
        fb3_1.setComment("Very interesting");
        fb3_1.setAuthor(userCoo2);
        fb3_1.setAuthorRole(Role.ROLE_COORDINATOR);
        fb3_1.setSubmissionTime(LocalDateTime.of(2019, 3, 20, 15, 55));

        Feedback fb3_2 = new Feedback();
        fb3_2.setComment("See attached file for comments");
        fb3_2.setAuthor(userSup1);
        fb3_2.setAuthorRole(Role.ROLE_SUPERVISOR);
        fb3_1.setFile(file1);
        fb3_1.setFileName("feedback");
        fb3_1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        fb3_2.setSubmissionTime(LocalDateTime.of(2019, 4, 3, 10, 1));

        Submission sub3_1 = new Submission();
        sub3_1.setType(SubmissionType.PROJECT_DESCRIPTION);
        sub3_1.setSubmittedDocument(doc3_1);
        sub3_1.setFeedbacks(Arrays.asList(fb3_1));
        sub3_1.setGrade(1f);

        Submission sub3_2 = new Submission();
        sub3_2.setType(SubmissionType.PROJECT_PLAN);
        sub3_2.setSubmittedDocument(doc3_2);
        sub3_2.setFeedbacks(Arrays.asList(fb3_2));
        sub3_2.setGrade(1f);

        Submission sub3_3 = new Submission();
        sub3_3.setType(SubmissionType.REPORT);
        sub3_3.setSubmittedDocument(doc3_3);
        
        Thesis thesis3 = new Thesis();
        thesis3.setStudent(userStuopprea4);
        thesis3.setCoordinator(userCoo2);
        thesis3.setSemester(sem2);
        thesis3.setSupervisor(userSup1);
        thesis3.setSubmissions(Arrays.asList(sub3_1, sub3_2, sub3_3));
        sub3_1.setThesis(thesis3);
        sub3_2.setThesis(thesis3);
        sub3_3.setThesis(thesis3);
        thesisRepository.save(thesis3);

        // Thesis 4
        Document doc4_1 = new Document();
        doc4_1.setAuthor(userStuopp2);
        doc4_1.setFile(file1);
        doc4_1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc4_1.setFileName("Computer Science as a hobby");
        doc4_1.setSubmissionTime(LocalDateTime.of(2019, 3, 26, 8, 6));

        Submission sub4_1 = new Submission();
        sub4_1.setSubmittedDocument(doc4_1);
        sub4_1.setType(SubmissionType.PROJECT_DESCRIPTION);

        Thesis thesis4 = new Thesis();
        thesis4.setStudent(userStuopp2);
        thesis4.setSemester(sem2);
        thesis4.setSubmissions(Arrays.asList(sub4_1));
        sub4_1.setThesis(thesis4);
        thesisRepository.save(thesis4);

        // Thesis 5
        Document doc5_1 = new Document();
        doc5_1.setFile(file1);
        doc5_1.setSubmissionTime(LocalDateTime.of(2019, 3, 26, 15, 1));
        doc5_1.setFileName("A study on memory cards");
        doc5_1.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc5_1.setAuthor(userStuopp3);

        Document doc5_2 = new Document();
        doc5_2.setFile(file1);
        doc5_2.setSubmissionTime(LocalDateTime.of(2019, 4, 8, 23, 20));
        doc5_2.setFileName("A study on memory cards - Plan");
        doc5_2.setFileType(MediaType.APPLICATION_PDF_VALUE);
        doc5_2.setAuthor(userStuopp3);

        Feedback fb5_1 = new Feedback();
        fb5_1.setAuthor(userCoo1);
        fb5_1.setAuthorRole(Role.ROLE_COORDINATOR);
        fb5_1.setComment("Ok");
        fb5_1.setSubmissionTime(LocalDateTime.of(2019, 3, 27, 13, 1));

        Submission sub5_1 = new Submission();
        sub5_1.setType(SubmissionType.PROJECT_DESCRIPTION);
        sub5_1.setGrade(1f);
        sub5_1.setSubmittedDocument(doc5_2);
        sub5_1.setFeedbacks(Arrays.asList(fb5_1));

        Submission sub5_2 = new Submission();
        sub5_2.setType(SubmissionType.PROJECT_PLAN);
        sub5_2.setSubmittedDocument(doc5_2);

        Thesis thesis5 = new Thesis();
        thesis5.setStudent(userStuopp3);
        thesis5.setCoordinator(userCoo1);
        thesis5.setSemester(sem2);
        thesis5.setSupervisor(userSupRea2);
        thesis5.setSubmissions(Arrays.asList(sub5_1, sub5_2));
        sub5_1.setThesis(thesis5);
        sub5_2.setThesis(thesis5);
        thesisRepository.save(thesis5);
    }
}