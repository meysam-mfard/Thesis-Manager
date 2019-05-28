package e.group.thesismanager.service;

import e.group.thesismanager.model.User;

public interface UserService {

    User getUserById(Long id);

    User getUserByUsername(String useName);

    User getCurrentUser();
}
