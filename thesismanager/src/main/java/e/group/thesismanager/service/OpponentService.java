package e.group.thesismanager.service;

import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;

public interface OpponentService {

    Thesis getThesis(User user);
}