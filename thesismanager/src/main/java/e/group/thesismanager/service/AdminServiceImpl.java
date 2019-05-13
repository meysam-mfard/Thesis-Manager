package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersByLastName(String lastName) {

        return userRepository.findUsersByLastName(lastName);
    }

    @Override
    public List<User> findUsersByRole(Role role) {

        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    @Override
    public User findUserById(Long id) {

        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist. Id: " + id));
    }

    @Override
    public User saveUser(User user) throws Exception {

        if (usernameExist(user.getUsername())) {
            //todo: throw customized exception
            log.error("Username already exists! Username: "+user.getUsername());
            throw new Exception("Username already exists!");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {

        if (!userRepository.findById(id).isPresent())
            throw new NotFoundException("User does not exist. Id: " + id);

        userRepository.deleteById(id);
    }

    @Override
    public User assignRoleToUserById(Long id, Role role) {

        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist. Id: " + id));

        user.getRoles().add(role);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User does not exist. Username: " + username));
    }

    @Override
    public Boolean usernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User updateUser(User updateUser) throws Exception {
        User user = userRepository.findById(updateUser.getId()).orElseThrow(() ->
                new NotFoundException("User does not exist. Id: " + updateUser.getId()));

        if (!user.getUsername().equals(updateUser.getUsername())
                && usernameExist(updateUser.getUsername())) {
            //todo: throw customized exception
            log.error("Username already exists! Username: "+updateUser.getUsername());
            throw new Exception("Username already exists!");
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String firstName, String lastName, String username) {
        return userRepository.findAllByFirstNameLikeAndLastNameLikeAndUsernameLike(firstName, lastName, username);
    }
}