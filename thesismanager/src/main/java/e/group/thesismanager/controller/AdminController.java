package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
public class AdminController {

    private final static String SAVE_USER_SUCCESS = "success";
    private final static String SAVE_USER_FAIL = "fail";

    //////////////////////////////////////////////
    //prevents changing User Id (Id is allocated automatically by DB)
    @InitBinder
    public void setDisallowedFields(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("user")
    public User emptyUser(Model model) {
        return new User();
    }

    //runs for every controller
    /*@ModelAttribute("userList")
    public Collection<User> populateUserList(Model model) {
        return adminService.findAllUsers();
    }*/
    //////////////////////////////////////////////

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("admin")
    public String getAdminHome(Model model) {
        model.addAttribute("userList", adminService.findAllUsers());
        return "pages/admin";
    }

    @PostMapping("admin/newuser")
    public String newUser(@ModelAttribute("user") User user){
        try {
            adminService.saveUser(user);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
        }

        return "redirect:/admin";//return "redirect:/admin?" + SAVE_USER_SUCCESS;
    }

    @PostMapping("updateuser")
    public String updateUser(@ModelAttribute User updateUser) {
        try {
            adminService.updateUser(updateUser);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
        }

        return "redirect:/pages/admin/";
    }
}
