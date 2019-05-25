package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }

    @GetMapping({"","/","index","home"})
    public String getHome(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "home";
    }
}
