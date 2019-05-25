package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.security.CustomUserDetailsService;
import e.group.thesismanager.service.OpponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
public class OpponentController {

    private final OpponentService opponentService;
    private final CustomUserDetailsService customUserDetailsService;

    public OpponentController(OpponentService opponentService, CustomUserDetailsService customUserDetailsService) {

        this.opponentService = opponentService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return customUserDetailsService.getCurrentUser();
    }

    @GetMapping("opponent")
    public String getOpponentHome(Model model) {

        User opponent = customUserDetailsService.getCurrentUser();
        model.addAttribute("thesis", opponentService.getThesis(opponent));
        return "pages/opponent";
    }
}