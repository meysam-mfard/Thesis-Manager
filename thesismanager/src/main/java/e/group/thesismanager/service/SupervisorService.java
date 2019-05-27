package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface SupervisorService {

    List<Thesis> getThesis(User user);

    List<Thesis> getRequests(User user);

    Thesis replyOnSupervisionProposition(Long thesisId, User supervisor, SupervisorRequestStatus answer)throws MissingRoleException;
}