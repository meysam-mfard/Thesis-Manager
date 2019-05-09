package e.group.thesismanager.repository;

import e.group.thesismanager.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findThesisById(Long thesisId);
}