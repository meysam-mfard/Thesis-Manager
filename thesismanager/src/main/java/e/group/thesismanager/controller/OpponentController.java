package e.group.thesismanager.controller;

import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.OpponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Slf4j
@Controller
public class OpponentController {

    private final static String SAVE_SUBMSSION_SUCCESS = "success";
    private final static String SAVE_SUBMSSION_FAIL = "fail";

    private final OpponentService opponentService;

    public OpponentController(OpponentService opponentService) {

        this.opponentService = opponentService;
    }

    /*
    @ModelAttribute("thesis")
    public Thesis emptyThesis(Model model) {

        return new Thesis();
    }
    */

    @GetMapping("opponent")
    public String getOpponentHome(Model model) {

        User opponent = opponentService.getUserByUsername(getCurrentUsername());
        model.addAttribute("thesis", opponentService.getThesis(opponent));
        return "pages/opponent";
    }

    @PostMapping("opponent/newFeedback")
    public String newFeedback(Long submissionId, String comment, Byte[] file, User author) {

        try {

            opponentService.feedbackOnSubmission(submissionId, comment, file, author, LocalDateTime.now(), Role.ROLE_OPPONENT);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/opponent?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/opponent?" + SAVE_SUBMSSION_SUCCESS;
    }

    @PostMapping("opponent/editFeedback")
    public String editFeedback(Long submissionId, Long feedbackId,String comment, Byte[] file, User author) {

        try {

            opponentService.editFeedbackOnSubmission(submissionId, feedbackId, comment, file, author, LocalDateTime.now(), Role.ROLE_OPPONENT);
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/opponent?" + SAVE_SUBMSSION_FAIL;
        }

        return "redirect:/opponent?" + SAVE_SUBMSSION_SUCCESS;
    }

    @GetMapping("opponent/feedback/{id}/edit")
    public String editFeedback(Model model, @PathVariable Long id) {

        Thesis thesis = opponentService.getThesisById(Long.valueOf(id));
        model.addAttribute("thesis", thesis);
        return "pages/editFeedback";
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