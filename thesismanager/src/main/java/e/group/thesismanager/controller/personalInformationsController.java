package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class personalInformationsController {

    private final UserService userService;

    public personalInformationsController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("personalInformations")
    public String getPersonalInformationsPage(Model model) {

        User user = userService.getCurrentUser();
        model.addAttribute("user", user);

        return "pages/personalInformations";
    }

    @PostMapping("personalInformations/changePassword")
    public String postNewPassword(@RequestParam(value = "currentPassword") String currentPassword,
                                  @RequestParam(value = "newPassword") String newPassword,
                                  @RequestParam(value = "newPasswordAgain") String newPasswordAgain) {

        User user = userService.getCurrentUser();

        try {

            userService.changePassword(user, currentPassword, newPassword, newPasswordAgain);

        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/personalInformations?" + "passwordChange=fail";
        }

        return "redirect:/personalInformations?" + "passwordChange=success";
    }
}