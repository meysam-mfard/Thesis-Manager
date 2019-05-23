package e.group.thesismanager.controller;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Document;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.repository.UserRepository;
import e.group.thesismanager.service.FeedbackService;
import e.group.thesismanager.service.StudentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThesisController {

    private final StudentService studentService;
    private final UserRepository userRepository;
    private final FeedbackService feedbackService;

    public ThesisController(StudentService studentService, UserRepository userRepository, FeedbackService feedbackService) {
        this.studentService = studentService;
        this.userRepository = userRepository;
        this.feedbackService = feedbackService;
    }

    @GetMapping("student/{studentId}/thesis")
    public String getThesis(Model model, @PathVariable Long studentId) throws MissingRoleException {
        model.addAttribute("thesis", studentService.getThesisForActiveSemesterByStudentId(studentId));
        model.addAttribute("studentId", studentId);
        return "pages/thesis";
    }

    @GetMapping("/submission/view")
    public String viewSubmission(Model model, @RequestParam(name = "stdId") Long studentId
            , @RequestParam(name = "subId") Long submissionId) throws MissingRoleException {
        Submission submission = studentService.getThesisForActiveSemesterByStudentId(studentId).getSubmissions().stream()
                .filter(tempSub -> tempSub.getId().equals(submissionId)).findFirst().orElseThrow(() ->
                        new NotFoundException("Submission does not exist. Id: " + submissionId));
        model.addAttribute("submission", submission);
        model.addAttribute("studentId", studentId);
        return "pages/viewSubmission";
    }

    @GetMapping("/submission/download")
    public ResponseEntity<Resource> downloadSubmission(Model model, @RequestParam(name = "stdId") Long studentId
            , @RequestParam(name = "subId") Long submissionId) {
        Submission submission = studentService.getThesisById(studentId).getSubmissions().stream()
                .filter(tempSub -> tempSub.getId().equals(submissionId)).findFirst().orElseThrow(() ->
                        new NotFoundException("Submission does not exist. Id: " + submissionId));
        Document document = submission.getSubmittedDocument();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(document.getFile()));
    }
/*
    @GetMapping("submission/feedback")
    public String viewSubmissionFeedbackPage(Model model, @RequestParam(name = "userId") Long userId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute Role authorRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist. ID: "+userId));
        Submission submission = studentService.getThesisById(studentId).getSubmissions().stream()
                .filter(tempSub -> tempSub.getId().equals(submissionId)).findFirst().orElseThrow(() ->
                        new NotFoundException("Submission does not exist. ID: " + submissionId));

        model.addAttribute("user", user);
        model.addAttribute("submission", submission);
        model.addAttribute("role", authorRole);
        return "pages/feedbackSubmissionForm";
    }

    @PostMapping("submission/comment")
    public void giveFeedbackComment(Model model, @RequestParam(name = "authId") Long authorId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute String comment) {
        supervisorService.feedbackCommentOnSubmission(submissionId, comment, authorId, Role.ROLE_SUPERVISOR);
    }

    @PostMapping("submission/sendFile")
    public void giveFeedbackFile(Model model, @RequestParam(name = "authId") Long authorId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute Feedback feedback
            , @ModelAttribute MultipartFile file) throws IOException {
        supervisorService.feedbackFileOnSubmission(submissionId, file, authorId, Role.ROLE_SUPERVISOR);
    }

    @PostMapping("submission/assess")
    public void giveFeedbackAssessment(Model model, @RequestParam(name = "authId") Long authorId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute Float grade) {
        supervisorService.assessSubmission(submissionId, grade);
    }*/
}
