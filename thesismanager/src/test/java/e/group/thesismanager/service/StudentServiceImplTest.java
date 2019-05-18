package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentServiceImplTest {
    private ThesisRepository mockThesisRepository;
    private SubmissionRepository mockSubmissionRepository;

    private StudentServiceImpl sut;

    @BeforeEach
    void setUp() {
        mockThesisRepository = Mockito.mock(ThesisRepository.class);
        mockSubmissionRepository = Mockito.mock(SubmissionRepository.class);

        sut = new StudentServiceImpl(mockThesisRepository, mockSubmissionRepository);
    }

    @AfterEach
    void tearDown() {
        mockThesisRepository = null;
        mockSubmissionRepository = null;
        sut = null;
    }

    @Test
    void initThesisNotStudentShouldThrowException() {
        assertThrows(MissingRoleException.class, ()->{
            User user = new User();
            Semester semester = new Semester();

            sut.initThesis(user, semester);
        });
    }

    @Test
    void initThesis() throws MissingRoleException {
        User user = new User();
        user.getRoles().add(Role.ROLE_STUDENT);
        Semester semester = new Semester();

        sut.initThesis(user, semester);
    }
}