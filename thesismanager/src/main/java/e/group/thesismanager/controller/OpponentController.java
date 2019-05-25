package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.OpponentService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
public class OpponentController {

    private final OpponentService opponentService;
    private final UserService userService;

    public OpponentController(OpponentService opponentService, UserService userService) {

        this.opponentService = opponentService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return userService.getCurrentUser();
    }

    @GetMapping("opponent")
    public String getOpponentHome(Model model) {

        User opponent = userService.getCurrentUser();
        model.addAttribute("thesis", opponentService.getThesis(opponent));
        return "pages/opponent";
    }
}