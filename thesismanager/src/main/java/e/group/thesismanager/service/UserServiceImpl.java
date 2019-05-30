package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Override
    public User getUserByUsername(String useName) {
        return userRepository.findByUsername(useName).orElseThrow(() ->
                new NotFoundException("User does not exist. Username: "+useName));
    }

    @Override
    public User getCurrentUser() {

        /*return userRepository.findByUsername(getCurrentUsername()).orElseThrow(() ->
                new NotFoundException("User does not exist. Username: " + getCurrentUsername()));*/
        return userRepository.findByUsername(getCurrentUsername()).orElse(null);
    }

    @Override
    public User changePassword(User user, String currentPassword, String newPassword, String newPasswordAgain) {

        String securityPrefix = "{noop}";

        if(!newPassword.equals(newPasswordAgain)) {

            throw new IllegalArgumentException("The new passwords are not the same!");
        }

        if(!user.getPassword().substring(securityPrefix.length()).equals(currentPassword)) {

            throw new IllegalArgumentException("Current password is wrong!");
        }

        user.setPassword(securityPrefix + newPassword);

        return userRepository.save(user);
    }

    private String getCurrentUsername() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
