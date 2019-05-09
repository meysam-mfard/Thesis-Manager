package e.group.thesismanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("admin")
    public String getAdmin() {
        return "pages/admin";
    }
}
