package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.OpponentService;
import e.group.thesismanager.service.SearchService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class OpponentController {

    private final OpponentService opponentService;
    private final UserService userService;
    private final SearchService searchService;

    public OpponentController(OpponentService opponentService, UserService userService, SearchService searchService) {

        this.opponentService = opponentService;
        this.userService = userService;
        this.searchService = searchService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return userService.getCurrentUser();
    }

    @ModelAttribute("searchedUser")
    public User emptyUser(Model model) {

        return new User();
    }

    @GetMapping("opponent")
    public String getOpponentHome(Model model) {

        User opponent = userService.getCurrentUser();
        model.addAttribute("thesisList", opponentService.getThesis(opponent));
        return "pages/opponent";
    }

    @PostMapping("opponent/search")
    public String searchThesis(Model model, @ModelAttribute("searchedUser") User user){

        model.addAttribute("searchedThesisList", searchService.searchThesisForOpponent("%" + user.getFirstName() + "%",
                "%" + user.getLastName() + "%"));

        model.addAttribute("thesisList", opponentService.getThesis(userService.getCurrentUser()));

        return "pages/opponent";
    }
}