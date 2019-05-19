package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StudentServiceImplTest {
    private ThesisRepository mockThesisRepository;
    private SubmissionRepository mockSubmissionRepository;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        mockThesisRepository = Mockito.mock(ThesisRepository.class);
        mockSubmissionRepository = Mockito.mock(SubmissionRepository.class);

        studentService = new StudentServiceImpl(mockThesisRepository, mockSubmissionRepository);
    }

    @AfterEach
    void tearDown() {
        mockThesisRepository = null;
        mockSubmissionRepository = null;
        studentService = null;
    }

    @Test
    void initThesisNotStudentShouldThrowException() {
        assertThrows(MissingRoleException.class, ()->{
            User user = new User();
            Semester semester = new Semester();

            studentService.initThesis(user, semester);
        });
    }

    @Test
    void initThesis() throws MissingRoleException {
        User user = new User();
        user.getRoles().add(Role.ROLE_STUDENT);
        Semester semester = new Semester();

        studentService.initThesis(user, semester);
    }

    @Test
    void submitReport() {
        final Long ID = 1L;
        User student = new User();
        student.setId(ID);
        student.getRoles().add(Role.ROLE_STUDENT);

        //creating a Byte array file
        String str = "fake file content";
        Byte[] file = new Byte[str.getBytes().length];
        int i = 0;
        for (byte b : str.getBytes()){
            file[i++] = b;
        }

        Document doc1 = new Document();
        doc1.setId(ID);
        doc1.setAuthor(student);
        doc1.setComment("This is my project description");
        doc1.setFile(file);
        /*Submission submission = new Submission();
        submission.setId(ID);
        submission.setType(SubmissionType.REPORT);
        submission.setSubmittedDocument(doc1);*/

        //Thesis
        Thesis thesis = new Thesis();
        thesis.setId(ID);
        //thesis.addSubmission(submission);

        ArgumentCaptor<Thesis> argumentCaptor = ArgumentCaptor.forClass(Thesis.class);

        studentService.submitReport(thesis, doc1);

        verify(mockThesisRepository, times(1)).save(argumentCaptor.capture());
        Thesis savedThesis = argumentCaptor.getValue();
        assertEquals(file.length, savedThesis.getSubmissions().get(0).getSubmittedDocument().getFile().length);

    }
}