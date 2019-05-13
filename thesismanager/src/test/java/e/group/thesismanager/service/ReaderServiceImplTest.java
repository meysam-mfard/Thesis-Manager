package e.group.thesismanager.service;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.repository.FeedbackRepository;
import e.group.thesismanager.repository.SubmissionRepository;
import e.group.thesismanager.repository.ThesisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ReaderServiceImplTest {

    ReaderService readerService;
    User mockedReader = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.READER)));
    private static final List<Thesis> THESIS_LIST = new LinkedList<>();

    @Mock
    ThesisRepository thesisRepository;
    @Mock
    FeedbackRepository feedbackRepository;
    @Mock
    SubmissionRepository submissionRepository;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.initMocks(this);
        readerService = new ReaderServiceImpl(thesisRepository, feedbackRepository, submissionRepository);
        User reader = new User("U1", "U11", "U111", "U1111", new HashSet<>(Arrays.asList(Role.READER)));
        Thesis thesis = new Thesis();
        thesis.setStudent(new User());
        thesis.setSemester(new Semester());
        thesis.getBidders().add(reader);

        thesisRepository.save(thesis);
        THESIS_LIST.add(thesis);
    }

    @Test
    void bidOnThesisErrorTest() {

        assertThrows(NotFoundException.class, () -> readerService.bidOnThesis(THESIS_LIST.get(0).getId(), mockedReader));
    }

    @Test
    void bidOnThesisSuccessTest() throws MissingRoleException {

        Thesis result = new Thesis();
        when(thesisRepository.findById(result.getId())).thenReturn(Optional.of(result));
        readerService.bidOnThesis(result.getId(), mockedReader);
        result.setStudent(THESIS_LIST.get(0).getStudent());
        result.setSemester(THESIS_LIST.get(0).getSemester());

        assertEquals(THESIS_LIST.get(0), result);
    }
}