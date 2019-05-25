package e.group.thesismanager.controller;

import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.SupervisorService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SupervisorController {

    private final static String SAVE_SUBMISSION_SUCCESS = "success";
    private final static String SAVE_SUBMISSION_FAIL = "fail";

    private final SupervisorService supervisorService;
    private final UserService userService;

    public SupervisorController(SupervisorService supervisorService, UserService userService) {

        this.supervisorService = supervisorService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return userService.getCurrentUser();
    }

    @GetMapping("supervisor")
    public String getSupervisorHome(Model model) {

        User supervisor = userService.getCurrentUser();
        model.addAttribute("thesisList", supervisorService.getThesis(supervisor));
        model.addAttribute("supervisionRequestsList", supervisorService.getRequests(supervisor));
        return "pages/supervisor";
    }

    @PostMapping("supervisor/replyToSupervisionRequest/{thesisId}/{answer}")
    public String replyToSupervisionRequest(@PathVariable Long thesisId, @PathVariable String answer) {

        try {

            supervisorService.replyOnSupervisionProposition(thesisId, userService.getCurrentUser(), SupervisorRequestStatus.fromString(answer));
        } catch (Exception e) {

            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/supervisor?" + SAVE_SUBMISSION_FAIL;
        }

        return "redirect:/supervisor?" + SAVE_SUBMISSION_SUCCESS;
    }
}