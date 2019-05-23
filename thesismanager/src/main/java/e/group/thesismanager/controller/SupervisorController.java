package e.group.thesismanager.controller;

import e.group.thesismanager.model.Feedback;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.SupervisorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Controller
public class SupervisorController {

    private final static String SAVE_SUBMSSION_SUCCESS = "success";
    private final static String SAVE_SUBMSSION_FAIL = "fail";

    private final SupervisorService supervisorService;

    public SupervisorController(SupervisorService supervisorService) {

        this.supervisorService = supervisorService;
    }

    @GetMapping("supervisor")
    public String getSupervisorHome(Model model) {

        User supervisor = supervisorService.getUserByUsername(getCurrentUsername());
        model.addAttribute("thesisList", supervisorService.getThesis(supervisor));
        model.addAttribute("supervisionRequestsList", supervisorService.getRequests(supervisor));
        return "pages/supervisor";
    }

    @PostMapping("supervisor/newFeedback")
    public String newFeedback(Long submissionId, String comment, byte[] file, String fileName, String fileType, User author) {

        try {

            supervisorService.feedbackOnSubmission(submissionId, comment, file, fileName, fileType, author, LocalDateTime.now(), Role.ROLE_SUPERVISOR);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMSSION_SUCCESS;
    }

    @PostMapping("supervisor/submission/comment")
    public void giveFeedbackComment(Model model, @RequestParam(name = "authId") Long authorId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute String comment) {
        supervisorService.feedbackCommentOnSubmission(submissionId, comment, authorId, Role.ROLE_SUPERVISOR);
    }

    @PostMapping("supervisor/submission/sendFile")
    public void giveFeedbackFile(Model model, @RequestParam(name = "authId") Long authorId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute Feedback feedback
            , @ModelAttribute MultipartFile file) throws IOException {
        supervisorService.feedbackFileOnSubmission(submissionId, file, authorId, Role.ROLE_SUPERVISOR);
    }

    @PostMapping("supervisor/submission/assess")
    public void giveFeedbackAssessment(Model model, @RequestParam(name = "authId") Long authorId
            , @RequestParam(name = "subId") Long submissionId
            , @ModelAttribute Float grade) {
        supervisorService.assessSubmission(submissionId, grade);
    }

    @PostMapping("supervisor/editFeedback")
    public String editFeedback(Long submissionId, Long feedbackId,String comment, byte[] file, String fileName, String fileType, User author) {

        try {

            supervisorService.editFeedbackOnSubmission(submissionId, feedbackId, comment, file, fileName, fileType, author, LocalDateTime.now(), Role.ROLE_SUPERVISOR);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMSSION_SUCCESS;
    }

    @GetMapping("supervisor/feedback/{id}/edit")
    public String editFeedback(Model model, @PathVariable Long id) {

        Thesis thesis = supervisorService.getThesisById(Long.valueOf(id));
        model.addAttribute("thesis", thesis);
        return "pages/editFeedback";
    }

    @PostMapping("supervisor/newAssessment")
    public String newAssessment(Long submissionId, Float grade) {

        try {

            supervisorService.assessSubmission(submissionId, grade);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMSSION_SUCCESS;
    }

    @PostMapping("supervisor/editAssessment")
    public String editAssessment(Long submissionId, Float grade) {

        try {

            supervisorService.editAssessmentSubmission(submissionId, grade);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMSSION_SUCCESS;
    }

    @GetMapping("supervisor/assessment/{id}/edit")
    public String editAssessment(Model model, @PathVariable Long id) {

        Thesis thesis = supervisorService.getThesisById(Long.valueOf(id));
        model.addAttribute("thesis", thesis);
        return "pages/editAssessment";
    }

    @PostMapping("supervisor/replyToSupervisionRequest")
    public String replyToSupervisionRequest(Long thesisId, User supervisor, boolean answer) {

        try {

            supervisorService.replyOnSupervisionProposition(thesisId, supervisor, answer);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMSSION_SUCCESS;
    }

    private String getCurrentUsername() {

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return username = ((UserDetails)principal).getUsername();
        } else {
            return username = principal.toString();
        }
    }
}