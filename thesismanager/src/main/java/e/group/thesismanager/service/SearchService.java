package e.group.thesismanager.service;

import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.Thesis;

import java.util.List;

public interface SearchService {

    List<Thesis> searchThesisForOpponent(String firstName, String lastName);
    List<Thesis> searchThesisForSupervisor(String firstName, String lastName, SupervisorRequestStatus supervisorRequestStatus);
    List<Thesis> searchThesisForReader(String firstName, String lastName);
    List<Thesis> searchPossibleThesisForReader(String firstName, String lastName);
    List<Thesis> searchThesisForCoordinator(String firstName, String lastName);
}