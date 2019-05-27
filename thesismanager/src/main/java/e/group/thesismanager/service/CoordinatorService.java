package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public interface CoordinatorService {

    List<Thesis> getThesis();

    void initSemester(Year Year, SemesterPeriod semesterPeriod);

    Semester setAllDeadlines(LocalDateTime projectDescriptionDeadline,
                         LocalDateTime projectPlanDeadline,
                         LocalDateTime reportDeadline,
                         LocalDateTime finalReportDeadline);

    List<User> getStudents();

    List<User> getReaders();

    List<User> getOpponents();

    List<User> getSupervisors();

    Thesis assignSupervisor(String supervisorUsername, Long thesisId) throws MissingRoleException;

    Thesis assignReaders(List<String> readersUsername, Long thesisId) throws MissingRoleException;

    Thesis assignOpponent(String opponentUsername, Long thesisId) throws MissingRoleException;
}