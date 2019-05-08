package e.group.thesismanager.service;

import e.group.thesismanager.model.Feedback;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.Thesis;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class AssessmentServiceIT {

    @Autowired
    AssessmentService assessmentService;

    @Test
    void feedbackOnSubmissionTest() {
        final String COMMENT = "sample comment 123456";
        Thesis thesis = assessmentService.getThesis().get(0);
        Integer submiossionNum = thesis.getSubmissions().size();
        Submission submission = thesis.getSubmissions().get(0);
        Long submissionId = submission.getId();
        Feedback feedback = new Feedback();
        feedback.setComment(COMMENT);
        assessmentService.feedbackOnSubmission(submission.getId(), feedback);

        Submission newSubmission = assessmentService.getThesis().stream().filter(th -> th.getId().equals(thesis.getId()))
                .findFirst().get()
                .getSubmissions().stream().filter(sub -> sub.getId().equals(submissionId))
                .findFirst().get();

        assertEquals(submiossionNum+1, thesis.getSubmissions().size());
        assertEquals(1, newSubmission.getFeedbacks().stream().filter(filter ->
                filter.getComment().equals(COMMENT)).count());
    }
}