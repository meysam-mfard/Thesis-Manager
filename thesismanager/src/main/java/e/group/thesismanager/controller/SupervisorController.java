package e.group.thesismanager.controller;

import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.SupervisorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
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
    public String getSupervisorHome(Model model, User user) {

        model.addAttribute("thesisList", supervisorService.getThesis(user));
        model.addAttribute("supervisionRequestsList", supervisorService.getRequests(user));
        return "pages/supervisor";
    }

    @PostMapping("supervisor/newFeedback")
    public String newFeedback(Long submissionId, String comment, File file, User author) {

        try {

            supervisorService.feedbackOnSubmission(submissionId, comment, file, author, LocalDateTime.now(), Role.ROLE_SUPERVISOR);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMSSION_SUCCESS;
    }

    @PostMapping("supervisor/editFeedback")
    public String editFeedback(Long submissionId, Long feedbackId,String comment, File file, User author) {

        try {

            supervisorService.editFeedbackOnSubmission(submissionId, feedbackId, comment, file, author, LocalDateTime.now(), Role.ROLE_SUPERVISOR);
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
}