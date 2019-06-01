package e.group.thesismanager.controller;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Document;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.DocumentRepository;
import e.group.thesismanager.service.FeedbackService;
import e.group.thesismanager.service.StudentService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
public class SubmissionController extends AbstractDocumentSubmission {

    private final StudentService studentService;
    private final UserService userService;
    private final FeedbackService feedbackService;

    public SubmissionController(StudentService studentService, UserService userService, FeedbackService feedbackService) {

        this.studentService = studentService;
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    @ModelAttribute("user")
    public User loggedInUser() {

        return userService.getCurrentUser();
    }

    @GetMapping("thesis")
    public String getThesis(Model model, @RequestParam(name = "stdId") Long studentId ) {

        model.addAttribute("thesis", studentService.getThesisByStudentId(studentId));
        model.addAttribute("studentId", studentId);
        return "pages/thesis";
    }

    @GetMapping("/submission/view")
    public String viewSubmission(Model model, @RequestParam(name = "subId") Long submissionId) {

        Submission submission = feedbackService.getSubmissionById(submissionId);
        model.addAttribute("submission", submission);
        model.addAttribute("studentId", submission.getThesis().getStudent().getId());
        return "pages/viewSubmission";
    }

    @GetMapping("/submission/download")
    public ResponseEntity<Resource> downloadSubmission(@RequestParam(name = "docId") Long documentId) {

        Document document = feedbackService.getDocument(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(document.getFile()));
    }

    @GetMapping("submission/feedback")
    public String viewFeedbackSubmissionPage(Model model, @RequestParam(name = "subId") Long submissionId
            , @RequestParam(name = "role") Role userRole) {

        Submission submission = feedbackService.getSubmissionById(submissionId);

        model.addAttribute("submission", submission);
        model.addAttribute("role", userRole);
        return "pages/feedbackSubmissionFrom";
    }

    @PostMapping("submission/feedback")
    public String giveFeedback(Model model, @RequestParam(name = "subId") Long submissionId
            , @RequestParam(name = "role") Role userRole
            , @RequestParam String comment
            , @RequestParam MultipartFile file) throws IOException, MissingRoleException {

        //Validating comment size, file type and file size
        String errorMessage = getCommentErrorMessageIfNotAcceptable(comment);
        if(errorMessage == null)
            errorMessage = getFileErrorMessageIfNotAcceptable(file);
        if( errorMessage != null) {
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "pages/error";
        }

        feedbackService.feedbackOnSubmission(submissionId, comment, file, userService.getCurrentUser().getId(), userRole);
        Submission submission = feedbackService.getSubmissionById(submissionId);

        model.addAttribute("submission", submission);
        model.addAttribute("studentId", submission.getThesis().getStudent().getId());
        return "pages/viewSubmission";
    }


    @GetMapping("submission/assess")
    public String viewAssessSubmissionPage(Model model, @RequestParam(name = "subId") Long submissionId
            , @RequestParam(name = "decGr") Boolean isGradeFloat /*or pass & fail*/) {

        Submission submission = feedbackService.getSubmissionById(submissionId);
        String studentName = submission.getThesis().getStudent().getFirstName() + " "
                + submission.getThesis().getStudent().getLastName();

        model.addAttribute("subType", submission.getType());
        model.addAttribute("subId", submission.getId());
        model.addAttribute("studentName", studentName);
        model.addAttribute("isGradeFloat", isGradeFloat);
        return "pages/assessSubmissionFrom";
    }

    @PostMapping("submission/assess")
    public String giveFeedbackAssessment(Model model, @RequestParam(name = "subId") Long submissionId
            , @RequestParam Float grade) {

        Submission submission = feedbackService.getSubmissionById(submissionId);
        feedbackService.assessSubmission(submissionId, grade);

        model.addAttribute("submission", submission);
        model.addAttribute("studentId", submission.getThesis().getStudent().getId());
        return "pages/viewSubmission";
    }
}