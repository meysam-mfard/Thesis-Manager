package e.group.thesismanager.controller;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.service.CoordinatorService;
import e.group.thesismanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Set;

@RequestMapping("coordinator")
@Controller
public class CoordinatorController {

    private final static String SUCCESS = "success";
    private final static String FAIL = "fail";

    private final CoordinatorService coordinatorService;
    private final UserService userService;

    public CoordinatorController(CoordinatorService coordinatorService, UserService userService) {

        this.coordinatorService = coordinatorService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return userService.getCurrentUser();
    }

    @GetMapping("coordinator")
    public String getCoordinatorHome(Model model) {

        model.addAttribute("semesters", coordinatorService.getSemesters());
        model.addAttribute("students", coordinatorService.getStudents());
        model.addAttribute("readers", coordinatorService.getReaders());
        model.addAttribute("opponents", coordinatorService.getOpponents());
        model.addAttribute("theses", coordinatorService.getThesis());
        model.addAttribute("submissionTypes", SubmissionType.values());

        return "pages/coordinator";
    }

    @PostMapping("coordinator/updateDeadline")
    public String postUpdateDeadline(@ModelAttribute SubmissionType type,
                                     @ModelAttribute LocalDateTime dateTime) {

        try {

        coordinatorService.setDeadline(type, dateTime);

         } catch (Exception e) {
        //todo: replace with validator
        e.printStackTrace();
        return "redirect:/coordinator?" + FAIL;
          }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/initializeSemester")
    public String postInitializeSemester(@ModelAttribute SemesterPeriod period,
                                         @ModelAttribute Year year) {
        try {

        coordinatorService.initSemester(year, period);

         } catch (Exception e) {
        //todo: replace with validator
        e.printStackTrace();
        return "redirect:/coordinator?" + FAIL;
          }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/assignReadersAndOpponent")
    public String postAssignReadersAndOpponent(@ModelAttribute Thesis thesis,
                                               @ModelAttribute Set<User> readers,
                                               @ModelAttribute User opponent) {

        try {

            coordinatorService.assignReaders(thesis, readers);
            coordinatorService.assignOpponent(thesis, opponent);
        } catch (MissingRoleException e) {
            //todo: replace with validator
            return "redirect:/coordinator?" + FAIL;
        }

        return "redirect:/coordinator?" + SUCCESS;
    }


}