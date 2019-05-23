package e.group.thesismanager.controller;

import e.group.thesismanager.model.*;
import e.group.thesismanager.service.CoordinatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@RequestMapping("coordinator")
@Controller
public class CoordinatorController {

    private CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

//    @GetMapping("")
//    public String getCoordinatorHome(Model model) {
//        model.addAttribute("semesters", coordinatorService.getSemesters());
//        model.addAttribute("students", coordinatorService.getStudents());
//        model.addAttribute("readers", coordinatorService.getReaders());
//        model.addAttribute("opponents", coordinatorService.getOpponents());
//        model.addAttribute("theses", coordinatorService.getThesis());
//        model.addAttribute("submissionTypes", SubmissionType.values());
//
//        return "pages/coordinator";
//    }
//
//    @PostMapping("/updateDeadline")
//    public String postUpdateDeadline(@ModelAttribute SubmissionType type,
//                                     @ModelAttribute LocalDateTime dateTime) {
//
//        coordinatorService.setDeadline(type, dateTime);
//
//        return "?updateDeadlineSuccess";
//    }
//
//    @PostMapping("/initializeSemester")
//    public String postInitializeSemester(@ModelAttribute SemesterPeriod period,
//                                         @ModelAttribute Year year) {
//
//        coordinatorService.initSemester(year, period);
//
//        return "?initializeSemesterSuccess";
//    }
//
//    @PostMapping("/assignReadersAndOpponent")
//    public String postAssignReadersAndOpponent(@ModelAttribute Thesis thesis,
//                                               @ModelAttribute List<User> readers,
//                                               @ModelAttribute User opponent) {
//
//        coordinatorService.assignReaders(thesis, readers);
//        coordinatorService.assignOpponent(thesis, opponent);
//
//        return "?assignReadersAndOpponentSuccess";
//    }
//
//    @PostMapping
//    public String postAssignGrade(@ModelAttribute Submission submission,
//                                  @ModelAttribute Float grade) {
//
//        coordinatorService.assignGrade(submission, grade);
//
//        return "?assignGradeSuccess";
//    }
}
