package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist. ID: " + id));
    }
}
