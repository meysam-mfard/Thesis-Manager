package e.group.thesismanager.controller;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Document;
import e.group.thesismanager.model.Submission;
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

    public ThesisController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("student/{studentId}/thesis")
    public String getThesis(Model model, @PathVariable Long studentId) {
        model.addAttribute("thesis", studentService.getThesis(studentId));
        model.addAttribute("studentId", studentId);
        return "pages/thesis";
    }

    @GetMapping("/submission/view")
    public String viewSubmission(Model model, @RequestParam(name = "stdId") Long studentId
            , @RequestParam(name = "subId") Long submissionId) {
        Submission submission = studentService.getThesis(studentId).getSubmissions().stream()
                .filter(tempSub -> tempSub.getId().equals(submissionId)).findFirst().orElseThrow(() ->
                        new NotFoundException("Submission does not exist. Id: " + submissionId));
        model.addAttribute("submission", submission);
        model.addAttribute("studentId", studentId);
        return "pages/viewSubmission";
    }

    @GetMapping("student/{studentId}/submission/{submissionId}/download")
    public ResponseEntity<Resource> downloadSubmission(Model model, @PathVariable Long studentId, @PathVariable Long submissionId) {
        Submission submission = studentService.getThesis(studentId).getSubmissions().stream()
                .filter(tempSub -> tempSub.getId().equals(submissionId)).findFirst().orElseThrow(() ->
                        new NotFoundException("Submission does not exist. Id: " + submissionId));
        Document document = submission.getSubmittedDocument();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(document.getFile()));
    }

    //Same functionality as downloadSubmission (Just for demonstration)
    @GetMapping("/submission/download")
    public ResponseEntity<Resource> downloadSubmission1(Model model, @RequestParam(name = "stdId") Long studentId
            , @RequestParam(name = "subId") Long submissionId) {
        Submission submission = studentService.getThesis(studentId).getSubmissions().stream()
                .filter(tempSub -> tempSub.getId().equals(submissionId)).findFirst().orElseThrow(() ->
                        new NotFoundException("Submission does not exist. Id: " + submissionId));
        Document document = submission.getSubmittedDocument();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(document.getFile()));
    }
}
