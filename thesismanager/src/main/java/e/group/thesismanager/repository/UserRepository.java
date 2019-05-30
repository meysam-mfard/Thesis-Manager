package e.group.thesismanager.repository;

import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByLastName(String lastName);

    Optional<User> findByUsername(String username);

    List<User> findAllByFirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCaseAndUsernameLike(String firstName, String lastName, String username);

    Optional<User> findUserByIdAndRolesContaining(Long userId, Role role);

    List<User> findAllByFirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(String firstName, String lastName);

    List<User> findAllByRolesContaining(Role role);

}