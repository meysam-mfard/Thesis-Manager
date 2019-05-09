package e.group.thesismanager.repository;

import e.group.thesismanager.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}