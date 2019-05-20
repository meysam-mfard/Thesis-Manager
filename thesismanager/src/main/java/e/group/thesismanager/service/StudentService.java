package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Document;
import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface StudentService {

    Thesis initThesis(User student, Semester semester) throws MissingRoleException;

    Thesis getThesis(Long id);

    Thesis getThesis(User student, Semester semester);

    List<Semester> getSemesters();

    void proposeSupervisor(Thesis thesis, User supervisor) throws MissingRoleException;

    void submitProjectDescription(Thesis thesis, Document projectDescription);

    void submitProjectPlan(Thesis thesis, Document projectPlan);

    void submitReport(Thesis thesis, Document report);

    void submitFinalReport(Thesis thesis, Document finalReport);
}