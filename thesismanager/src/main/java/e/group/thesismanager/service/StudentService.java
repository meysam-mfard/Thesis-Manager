package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Document;
import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

public interface StudentService {

    void initThesis(User student, Semester semester) throws MissingRoleException;

    Thesis getThesis(Long id);

    Thesis getThesis(User student, Semester semester);

    void proposeSupervisor(Thesis thesis, User supervisor);

    void submitProjectDescription(Thesis thesis, Document projectDescription);

    void submitProjectPlan(Thesis thesis, Document projectPlan);

    void submitReport(Thesis thesis, Document report);

    void submitFinalReport(Thesis thesis, Document finalReport);

}
