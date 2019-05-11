package e.group.thesismanager.repository;

import e.group.thesismanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByLastName(String lastName);

    Optional<User> findByUsername(String username);
}
