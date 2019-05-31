package e.group.thesismanager.service;

import e.group.thesismanager.model.User;

public interface UserService {

    User getUserById(Long id);

    User getCurrentUser();

    User changePassword(User user, String currentPassword, String newPassword, String newPasswordAgain);
}