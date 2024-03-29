package e.group.thesismanager.service;

import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;

import java.util.List;

public interface AdminService {

    List<User> findAllUsers();

    List<User> findUsersByLastName(String lastName);

    List<User> findUsersByRole(Role role);

    User findUserById(Long id);

    User saveUser(User user) throws Exception;

    void deleteUserById(Long id);

    User assignRoleToUserById(Long id, Role role);

    Boolean usernameExist(String username);

    User editUser(User updateUser) throws Exception;

    List<User> searchUser(String firstName, String lastName, String username);

    void activeUserById(Long id, Boolean activate);
}