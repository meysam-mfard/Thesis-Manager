package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.SemesterRepository;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoordinatorServiceImpl implements CoordinatorService {

    private SemesterRepository semesterRepository;
    private UserRepository userRepository;
    private ThesisRepository thesisRepository;
    private SemesterService semesterService;

    @Autowired
    public CoordinatorServiceImpl(ThesisRepository thesisRepository, UserRepository userRepository, SemesterRepository semesterRepository, SemesterService semesterService) {

        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
        this.thesisRepository = thesisRepository;
        this.semesterService = semesterService;
    }

    @Override
    public List<Thesis> getThesis() {

        return thesisRepository.findAll();
    }

    @Override
    public void initSemester(Year Year, SemesterPeriod semesterPeriod) {
        // Set all other semesters to not active
        List<Semester> previousSemesters = semesterService.getSemesters();

        for (Semester s : previousSemesters) {

            s.setActive(false);
        }

        // Create new semester and set it to active
        Semester semester = new Semester();
        semester.setYear(Year);
        semester.setSemesterPeriod(semesterPeriod);
        semester.setActive(true);
        semesterRepository.save(semester);
    }

    @Override
    public Semester setAllDeadlines(LocalDateTime projectDescriptionDeadline,
                            LocalDateTime projectPlanDeadline,
                            LocalDateTime reportDeadline,
                            LocalDateTime finalReportDeadline) {

        if ((projectDescriptionDeadline != null && projectDescriptionDeadline.isBefore(LocalDateTime.now())) ||
                (projectPlanDeadline != null && projectPlanDeadline.isBefore(LocalDateTime.now())) ||
                (reportDeadline != null && reportDeadline.isBefore(LocalDateTime.now())) ||
                (finalReportDeadline != null && finalReportDeadline.isBefore(LocalDateTime.now()))) {

            throw new IllegalArgumentException("One or more of the given deadlines have already passed!");
        }

        Semester semester = semesterService.getCurrentSemester();

        if((semester.getProjectDescriptionDeadline() == null && projectDescriptionDeadline != null) ||
                (semester.getProjectDescriptionDeadline() != null && projectDescriptionDeadline != null)) {

            semester.setProjectDescriptionDeadline(projectDescriptionDeadline);
        }

        if((semester.getProjectPlanDeadline() == null && projectPlanDeadline != null) ||
                (semester.getProjectPlanDeadline() != null && projectPlanDeadline != null)) {

            semester.setProjectPlanDeadline(projectPlanDeadline);
        }

        if((semester.getReportDeadline() == null && reportDeadline != null) ||
                (semester.getReportDeadline() != null && reportDeadline != null)) {

            semester.setReportDeadline(reportDeadline);
        }

        if((semester.getFinalReportDeadline() == null && finalReportDeadline != null) ||
                (semester.getFinalReportDeadline() != null && finalReportDeadline != null)) {

            semester.setFinalReportDeadline(finalReportDeadline);
        }

        return semesterRepository.save(semester);
    }

    @Override
    public List<User> getStudents() {

        return getUserByRole(Role.ROLE_STUDENT);
    }

    @Override
    public List<User> getReaders() {

        return getUserByRole(Role.ROLE_READER);
    }

    @Override
    public List<User> getOpponents() {

        return getUserByRole(Role.ROLE_OPPONENT);
    }

    @Override
    public List<User> getSupervisors() {

        return getUserByRole(Role.ROLE_SUPERVISOR);
    }

    @Override
    public Thesis assignSupervisor(String supervisorUsername, Long thesisId) throws MissingRoleException {

        User supervisor = userRepository.findByUsername(supervisorUsername).orElseThrow(() ->
                new NotFoundException("User does not exist. Username: " + supervisorUsername));

        if (!supervisor.getRoles().contains(Role.ROLE_SUPERVISOR)) {

            throw new MissingRoleException("Could not assign supervisor; User is not an supervisor!");
        }

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + thesisId));

        thesis.setSupervisor(supervisor);
        thesis.setSupervisorRequestStatus(SupervisorRequestStatus.ACCEPTED);
        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis assignOpponent(String opponentUsername, Long thesisId) throws MissingRoleException {

        User opponent = userRepository.findByUsername(opponentUsername).orElseThrow(() ->
                new NotFoundException("User does not exist. Username: " + opponentUsername));

        if (!opponent.getRoles().contains(Role.ROLE_OPPONENT)) {

            throw new MissingRoleException("Could not assign opponent; User is not an opponent!");
        }

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + thesisId));

        thesis.setOpponent(opponent);
        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis assignReaders(List<String> readersUsername, Long thesisId) throws MissingRoleException {

        List<User> readersList = new ArrayList<User>();

        for(String readerUsername : readersUsername) {

            User reader =  userRepository.findByUsername(readerUsername).orElseThrow(() ->
                    new NotFoundException("User does not exist. Username: " + readerUsername));

            readersList.add(reader);
        }

        for (User user : readersList) {

            if(!user.getRoles().contains(Role.ROLE_READER))
                throw new MissingRoleException("Could not assign readers; User is not a reader!");
        }

        Thesis thesis = thesisRepository.findById(thesisId).orElseThrow(() ->
                new NotFoundException("Thesis does not exist. Id: " + thesisId));

        thesis.getReaders().clear();
        thesis.getReaders().addAll(readersList);
        return thesisRepository.save(thesis);
    }

    private List<User> getUserByRole(Role role) {

        return userRepository.findAll().stream().filter(
                (u) -> u.getRoles().contains(role)).
                collect(Collectors.toList());
    }
}