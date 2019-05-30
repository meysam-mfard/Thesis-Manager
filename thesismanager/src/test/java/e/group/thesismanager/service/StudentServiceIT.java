package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.DocumentRepository;
import e.group.thesismanager.repository.SemesterRepository;
import e.group.thesismanager.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceIT {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Transactional
    @Test
    public void studentServiceTest_InitThesisNotNull() throws MissingRoleException {
        User student = userRepository.findAllByRolesContaining(Role.ROLE_STUDENT).get(0);
        studentService.initThesis(student, semesterRepository.findById(1L).get());

        Assertions.assertNotNull(studentService.getThesisById(1L));
    }

    @Transactional
    @Test
    public void studentServiceTest_SubmitProjectDescription() throws MissingRoleException {
        int expected = studentService.getThesisById(1L).getSubmissions().size() + 1;

        User student = userRepository.findAllByRolesContaining(Role.ROLE_STUDENT).get(0);
        studentService.initThesis( student, semesterRepository.findById(1L).get());
        studentService.submitProjectDescription(studentService.getThesisById(1L), documentRepository.findById(1L).get());

        int actual =studentService.getThesisById(1L).getSubmissions().size();

        Assertions.assertEquals(expected, actual);
    }
}