package e.group.thesismanager.service;

import e.group.thesismanager.model.Feedback;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.Thesis;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class OpponentServiceIT {

    @Autowired
    OpponentService opponentService;

    @Transactional
    @Test
    void feedbackOnSubmissionTest() {

        final String COMMENT = "sample comment 123456";
        Thesis thesis = opponentService.getThesis().get(0);
        Submission submission = thesis.getSubmissions().get(0);
        Integer feedbacksCount = submission.getFeedbacks().size();

        Long submissionId = submission.getId();
        Feedback feedback = new Feedback();
        feedback.setComment(COMMENT);
        opponentService.feedbackOnSubmission(submission.getId(), feedback);

        Submission newSubmission = opponentService.getThesis().stream().filter(th -> th.getId().equals(thesis.getId()))
                .findFirst().get()
                .getSubmissions().stream().filter(sub -> sub.getId().equals(submissionId))
                .findFirst().get();

        assertEquals(feedbacksCount+1, newSubmission.getFeedbacks().size());
        assertEquals(1, newSubmission.getFeedbacks().stream().filter(filter ->
                filter.getComment().equals(COMMENT)).count());
    }
}