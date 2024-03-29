package e.group.thesismanager.controller;

import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.SearchService;
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
    private final SearchService searchService;

    public SupervisorController(SupervisorService supervisorService, UserService userService, SearchService searchService) {

        this.supervisorService = supervisorService;
        this.userService = userService;
        this.searchService = searchService;
    }

    @ModelAttribute("user")
    public User loggedInUser() {

        return userService.getCurrentUser();
    }

    @ModelAttribute("searchedUser")
    public User emptyUser() {

        return new User();
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

    @PostMapping("supervisor/search")
    public String searchThesis(Model model, @ModelAttribute("searchedUser") User user){

        model.addAttribute("searchedThesisList", searchService.searchThesisForSupervisor("%" + user.getFirstName() + "%",
                "%" + user.getLastName() + "%", SupervisorRequestStatus.ACCEPTED));
        model.addAttribute("searchedRequestList", searchService.searchThesisForSupervisor("%" + user.getFirstName() + "%",
                "%" + user.getLastName() + "%", SupervisorRequestStatus.REQUEST_SENT));

        model.addAttribute("thesisList", supervisorService.getThesis(userService.getCurrentUser()));
        model.addAttribute("supervisionRequestsList", supervisorService.getRequests(userService.getCurrentUser()));

        return "pages/supervisor";
    }
}