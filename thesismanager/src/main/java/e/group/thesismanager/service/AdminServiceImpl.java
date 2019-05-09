package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public User saveUser(User user) {

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
}