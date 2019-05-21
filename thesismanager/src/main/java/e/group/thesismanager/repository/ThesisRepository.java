package e.group.thesismanager.repository;

import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisRepository extends JpaRepository<Thesis, Long> {

    List<Thesis> findThesesByStudent(User user);
    List<Thesis> findThesesByStudentAndSemester(User user, Semester semester);
    List<Thesis> findThesesByOpponent(User user);
    List<Thesis> findThesesBySupervisorAndSupervisorAccept(User user, boolean answer);
}