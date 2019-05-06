package e.group.thesismanager.repository;

import e.group.thesismanager.model.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesisRepository extends JpaRepository<Thesis, Long> {
}
