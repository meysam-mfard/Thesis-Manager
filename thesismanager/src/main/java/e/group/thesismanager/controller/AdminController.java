package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
public class AdminController {

    private final static String SAVE_USER_SUCCESS = "success";
    private final static String SAVE_USER_FAIL = "fail";

    @ModelAttribute("user")
    public User emptyUser() {
        return new User();
    }

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("admin")
    public String getAdminHome(Model model) {
        model.addAttribute("userList", adminService.findAllUsers());
        return "pages/admin";
    }

    @PostMapping("admin/newUser")
    public String newUser(@ModelAttribute("user") User user){
        try {
            adminService.saveUser(user);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/admin?"+SAVE_USER_FAIL;
        }

        return "redirect:/admin?" + SAVE_USER_SUCCESS;
    }

    @PostMapping("admin/editUser")
    public String updateUser(@ModelAttribute User updateUser) {
        try {
            adminService.editUser(updateUser);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/admin?"+SAVE_USER_FAIL;
        }

        return "redirect:/admin?"+SAVE_USER_SUCCESS;
    }

    @PostMapping("admin/search")
    public String searchUser(Model model, @ModelAttribute("user") User user){
        model.addAttribute("userList", adminService.searchUser("%"+user.getFirstName()+"%",
                "%"+user.getLastName()+"%", "%"+user.getUsername()+"%"));

        return "pages/admin";
    }

    @GetMapping("admin/{id}/edit")
    public String editUser(Model model, @PathVariable Long id) {
        User user = adminService.findUserById(id);
        model.addAttribute("user", user);
        return "pages/editUser";
    }

    @GetMapping("admin/{id}/inactivate")
    public String inactivateUser(@PathVariable Long id) {
        try {
            adminService.activeUserById(id, false);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/admin?"+SAVE_USER_FAIL;
        }

        return "redirect:/admin?"+SAVE_USER_SUCCESS;
    }

    @GetMapping("admin/{id}/activate")
    public String activateUser(@PathVariable Long id) {
        try {
            adminService.activeUserById(id, true);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/admin?"+SAVE_USER_FAIL;
        }

        return "redirect:/admin?"+SAVE_USER_SUCCESS;
    }
}
