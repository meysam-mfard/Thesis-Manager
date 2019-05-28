package e.group.thesismanager.repository;

import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.SubmissionType;
import e.group.thesismanager.model.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByThesisAndType(Thesis thesis, SubmissionType submissionType);
}
