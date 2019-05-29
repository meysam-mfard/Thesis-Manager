package e.group.thesismanager.controller;

import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.SemesterPeriod;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.CoordinatorService;
import e.group.thesismanager.service.SearchService;
import e.group.thesismanager.service.SemesterService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class CoordinatorController {

    private final static String SUCCESS = "success";
    private final static String FAIL = "fail";

    private final CoordinatorService coordinatorService;
    private final UserService userService;
    private final SemesterService semesterService;
    private final SearchService searchService;

    public CoordinatorController(CoordinatorService coordinatorService, UserService userService, SemesterService semesterService, SearchService searchService) {

        this.coordinatorService = coordinatorService;
        this.userService = userService;
        this.semesterService = semesterService;
        this.searchService = searchService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return userService.getCurrentUser();
    }

    @ModelAttribute("thesis")
    public Thesis emptyThesis(Model model) {

        return new Thesis();
    }

    @ModelAttribute("semester")
    public Semester emptySemester(Model model) {

        return new Semester();
    }

    @ModelAttribute("searchedUser")
    public User emptyUser(Model model) {

        return new User();
    }

    @GetMapping("coordinator")
    public String getCoordinatorHome(Model model) {

        addModelAttributes(model);

        return "pages/coordinator";
    }

    @PostMapping("coordinator/updateDeadline")
    public String postUpdateDeadline(@RequestParam(value = "projectDescriptionDeadline") String projectDescriptionDeadline,
                                     @RequestParam(value = "projectPlanDeadline") String projectPlanDeadline,
                                     @RequestParam(value = "reportDeadline") String reportDeadline,
                                     @RequestParam(value = "finalReportDeadline") String finalReportDeadline) {

        try {

            coordinatorService.setAllDeadlines(parseDateTimeString(projectDescriptionDeadline),
                    parseDateTimeString(projectPlanDeadline), parseDateTimeString(reportDeadline),
                    parseDateTimeString(finalReportDeadline));

        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/coordinator?" + FAIL;
        }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/initializeSemester")
    public String postInitializeSemester(@ModelAttribute("semesterPeriod") String semesterPeriod,
                                         @ModelAttribute Year year) {
        try {

            coordinatorService.initSemester(year, SemesterPeriod.fromString(semesterPeriod));

        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/coordinator?" + FAIL;
        }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/assignOpponent/{thesisId}")
    public String postAssignOpponent(@ModelAttribute("thesis") Thesis thesis, @PathVariable Long thesisId) {

        try {

            coordinatorService.assignOpponent(thesis.getOpponent().getUsername(), thesisId);
        } catch (MissingRoleException e) {
            //todo: replace with validator
            return "redirect:/coordinator?" + FAIL;
        }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/assignReaders/{thesisId}")
    public String postAssignReaders(@ModelAttribute("thesis") Thesis thesis, @PathVariable Long thesisId) {

        try {

            List<String> readersUsername = new ArrayList<String>();

            for (User reader : thesis.getReaders()) {

                readersUsername.add(reader.getUsername());
            }

            coordinatorService.assignReaders(readersUsername, thesisId);
        } catch (MissingRoleException e) {
            //todo: replace with validator
            return "redirect:/coordinator?" + FAIL;
        }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/assignSupervisor/{thesisId}")
    public String postAssignSupervisor(@ModelAttribute("thesis") Thesis thesis, @PathVariable Long thesisId) {

        try {

            coordinatorService.assignSupervisor(thesis.getSupervisor().getUsername(), thesisId);
        } catch (MissingRoleException e) {
            //todo: replace with validator
            return "redirect:/coordinator?" + FAIL;
        }

        return "redirect:/coordinator?" + SUCCESS;
    }

    @PostMapping("coordinator/search")
    public String searchThesis(Model model, @ModelAttribute("searchedUser") User user) {

        List<Thesis> foundList = searchService.searchThesisForCoordinator("%" + user.getFirstName() + "%",
                "%" + user.getLastName() + "%");

        if (foundList != null && !foundList.isEmpty()) {

            model.addAttribute("searchedThesisList", foundList);

            addModelAttributes(model);

            return "pages/coordinator";
        }

        return "redirect:/coordinator?" + "NoThesisFound";
    }

    private LocalDateTime parseDateTimeString(String dateTimeString) {


        if (!dateTimeString.isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            return LocalDateTime.parse(dateTimeString, formatter);
        }

        return null;
    }

    private void addModelAttributes(Model model) {

        model.addAttribute("readers", coordinatorService.getReaders());
        model.addAttribute("opponents", coordinatorService.getOpponents());
        model.addAttribute("supervisors", coordinatorService.getSupervisors());
        model.addAttribute("currentSemester", semesterService.getCurrentSemester());
        model.addAttribute("semesterPeriods", SemesterPeriod.values());

        List<Thesis> theses = coordinatorService.getThesis();
        for (Thesis thesis : theses) {

            if (thesis.getOpponent() == null) {

                thesis.setOpponent(new User());
            }

            if (thesis.getSupervisor() == null) {

                thesis.setSupervisor(new User());
            }
        }

        model.addAttribute("theses", theses);
    }
}