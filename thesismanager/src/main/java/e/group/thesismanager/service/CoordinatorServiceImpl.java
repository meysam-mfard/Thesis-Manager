package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Service
public class CoordinatorServiceImpl extends FeedbackService implements CoordinatorService {

    private SemesterRepository semesterRepository;
    private List<Thesis> thesisList;

    @Autowired
    public CoordinatorServiceImpl(ThesisRepository thesisRepository, FeedbackRepository feedbackRepository,
                                  SubmissionRepository submissionRepository, UserRepository userRepository,
                                  SemesterRepository semesterRepository) {

        super(thesisRepository, feedbackRepository, submissionRepository, userRepository);
        this.semesterRepository = semesterRepository;
    }

    @Override
    public Thesis assignOpponent(User student, User opponent) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not assign opponent; User is not a student");

        Thesis thesis = getThesisByStudent(student);
        thesis.setOpponent(opponent);
        return thesis;
    }

    @Override
    public Thesis assignSupervisor(User student, User supervisor) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not assign supervisor; User is not a student");
        if(!supervisor.getRoles().contains(Role.ROLE_SUPERVISOR))
            throw new MissingRoleException("Could not assign supervisor; User is not a supervisor");

        Thesis thesis = getThesisByStudent(student);
        thesis.setSupervisor(supervisor);
        return thesis;

    }

    @Override
    public Thesis evaluateProjectPlan(User student, Float grade) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not evaluate project plan; User is not a student");

        Thesis thesis = getThesisByStudent(student);
        List<Submission> submissions = thesis.getSubmissions();

        Submission evaluatedSubmission = findSubmissionByType(SubmissionType.PROJECT_PLAN, submissions);
        int index = submissions.indexOf(evaluatedSubmission);
        evaluatedSubmission.setGrade(grade);

        submissions.set(index, evaluatedSubmission);
        thesis.setSubmissions(submissions);
        return thesis;
    }

    @Override
    public Thesis gradeFinalProject(User student, Float grade) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not grade final project; User is not a student");

        Thesis thesis = getThesisByStudent(student);
        List<Submission> submissions = thesis.getSubmissions();

        Submission evaluatedSubmission = findSubmissionByType(SubmissionType.FINAL_REPORT, submissions);
        int index = submissions.indexOf(evaluatedSubmission);
        evaluatedSubmission.setGrade(grade);

        submissions.set(index, evaluatedSubmission);
        thesis.setSubmissions(submissions);
        return thesis;
    }

    @Override
    public Thesis initiateThesis(User student) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not initiate thesis; User is not a student");

        Thesis thesis = new Thesis();
        thesis.setStudent(student);
        thesis.setSemester(new Semester());
        return thesis;
    }

    @Override
    public void setThesis(List<Thesis> thesisList) {

        this.thesisList = thesisList;
    }

    private Submission findSubmissionByType(SubmissionType type, List<Submission> submissions) {

        Submission submissionToReturn = submissions.stream()
                .filter(s -> s.getType().equals(type))
                .findFirst()
                .orElseThrow(
                        ()-> new NotFoundException("Submission not found"));

        return submissionToReturn;
    }

    private Thesis getThesisByStudent(User student) throws MissingRoleException {

        if(!student.getRoles().contains(Role.ROLE_STUDENT))
            throw new MissingRoleException("Could not get thesis by student; User is not a student");

        this.thesisList = getThesis();

        return thesisList.stream()
                .filter(x -> x.getStudent().equals(student))
                .findFirst()
                .orElseThrow( ()-> new NotFoundException("User does not exist." + student.getFirstName() + student.getLastName()));
    }

    @Override
    public Semester initSemester(Year Year, SemesterPeriod semesterPeriod) {
        // Set all other semesters to not active
        List<Semester> previousSemesters = semesterRepository.findAll();
        for (Semester s : previousSemesters) {
            s.setActive(false);
        }

        // Create new semester and set it to active
        Semester semester = new Semester();
        semester.setYear(Year);
        semester.setSemesterPeriod(semesterPeriod);
        semester.setActive(true);
        semesterRepository.save(semester);

        return semester;
    }

    @Override
    public void setDeadline(SubmissionType type, LocalDateTime dateTime) {
        Semester semester = semesterRepository.findByActiveIsTrue();

        switch (type) {
            case PROJECT_DESCRIPTION:
                semester.setProjectDescriptionDeadline(dateTime);
                break;
            case PROJECT_PLAN:
                semester.setProjectPlanDeadline(dateTime);
                break;
            case REPORT:
                semester.setReportDeadline(dateTime);
                break;
            case FINAL_REPORT:
                semester.setFinalReportDeadline(dateTime);
                break;
        }
    }
}
