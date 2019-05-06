package e.group.thesismanager.service;

import e.group.thesismanager.repository.DocumentRepository;
import e.group.thesismanager.repository.SemesterRepository;
import e.group.thesismanager.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.beans.Transient;

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

    @Transient
    @Test
    public void studentServiceTest_InitThesisNotNull() {
        studentService.initThesis(userRepository.findById(1L).get(), semesterRepository.findById(1L).get());

        Assertions.assertNotNull(studentService.getThesis(1L));
    }

    @Transient
    @Test
    public void studentServiceTest_SubmitProjectDescription() {
        studentService.initThesis(userRepository.findById(1L).get(), semesterRepository.findById(1L).get());
        //studentService.submitProjectDescription(studentService.getThesis(1L), documentRepository.findById(1L).get());
        System.out.println(documentRepository.findById(1L).get());

        Assertions.assertEquals(studentService.getThesis(1L).getSubmissions().size(), 1);
    }
}