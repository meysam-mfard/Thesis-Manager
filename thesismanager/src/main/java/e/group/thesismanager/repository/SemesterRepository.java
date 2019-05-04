package e.group.thesismanager.repository;

import e.group.thesismanager.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
}
