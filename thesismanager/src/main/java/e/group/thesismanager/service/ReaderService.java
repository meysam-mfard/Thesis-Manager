package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface ReaderService {

    List<Thesis> getPossibleTheses(User user);

    List<Thesis> getAssignedTheses(User user);

    Thesis bidOnThesis(Long thesisId, User reader) throws MissingRoleException;
}