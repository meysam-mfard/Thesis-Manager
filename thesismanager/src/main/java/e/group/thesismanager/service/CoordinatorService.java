package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Set;

public interface CoordinatorService {

    List<Thesis> getThesis();

    void initSemester(Year Year, SemesterPeriod semesterPeriod);

    List<Semester> getSemesters();

    void setDeadline(SubmissionType type, LocalDateTime dateTime);

    List<User> getStudents();

    List<User> getReaders();

    List<User> getOpponents();

    void assignSupervisor(Thesis thesis, User supervisor) throws MissingRoleException;

    void assignReaders(Thesis thesis, Set<User> readers) throws MissingRoleException;

    void assignOpponent(Thesis thesis, User opponent) throws MissingRoleException;

    //Submission assessSubmission(Long submissionId, Float grade);

}
