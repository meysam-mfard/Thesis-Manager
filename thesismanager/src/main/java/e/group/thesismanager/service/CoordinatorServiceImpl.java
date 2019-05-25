package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CoordinatorServiceImpl implements CoordinatorService {

    private SemesterRepository semesterRepository;
    private UserRepository userRepository;
    private ThesisRepository thesisRepository;

    @Autowired
    public CoordinatorServiceImpl(ThesisRepository thesisRepository, UserRepository userRepository, SemesterRepository semesterRepository) {

        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
        this.thesisRepository = thesisRepository;
    }

    @Override
    public List<Thesis> getThesis() {

        return thesisRepository.findAll();
    }

    @Override
    public void initSemester(Year Year, SemesterPeriod semesterPeriod) {
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
    }

    @Override
    public List<Semester> getSemesters() {

        return semesterRepository.findAll();
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
    public void assignSupervisor(Thesis thesis, User supervisor) throws MissingRoleException {

        if(!supervisor.getRoles().contains(Role.ROLE_SUPERVISOR))
            throw new MissingRoleException("Could not assign supervisor; User is not a supervisor");

        thesis.setSupervisor(supervisor);
    }

    @Override
    public void assignReaders(Thesis thesis, Set<User> readers) throws MissingRoleException {

        for (User u : readers) {
            if(!u.getRoles().contains(Role.ROLE_READER))
                throw new MissingRoleException("Could not assign readers; User is not a reader");
        }

        thesis.setReaders(readers);
    }

    @Override
    public void assignOpponent(Thesis thesis, User opponent) throws MissingRoleException {

        if (!opponent.getRoles().contains(Role.ROLE_OPPONENT))
            throw new MissingRoleException("Could not assign opponent; User is not an opponent");

        thesis.setOpponent(opponent);
    }

    private List<User> getUserByRole(Role role) {

        return userRepository.findAll().stream().filter(
                (u) -> u.getRoles().contains(role)).
                collect(Collectors.toList());
    }
}