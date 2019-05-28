package e.group.thesismanager.service;

import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

import java.util.List;

public interface OpponentService {

    List<Thesis> getThesis(User user);
}