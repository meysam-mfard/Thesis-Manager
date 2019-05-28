package e.group.thesismanager.repository;

import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThesisRepository extends JpaRepository<Thesis, Long> {

    List<Thesis> findThesesByStudent(User user);
    List<Thesis> findThesesByStudentId(Long studentId);
    Optional<Thesis> findThesisByStudentAndSemester(User user, Semester semester);
    Optional<Thesis> findThesisByStudentIdAndSemesterActiveIsTrue(Long StudentId);
    List<Thesis> findThesisByOpponent(User user);
    List<Thesis> findThesesBySupervisorAndSupervisorRequestStatus(User user, SupervisorRequestStatus status);
    List<Thesis> findThesesByReaders(User user);
}