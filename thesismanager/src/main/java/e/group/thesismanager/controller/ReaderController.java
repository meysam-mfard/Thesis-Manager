package e.group.thesismanager.controller;

import e.group.thesismanager.model.User;
import e.group.thesismanager.service.ReaderService;
import e.group.thesismanager.service.SearchService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
public class ReaderController {

    private final static String SAVE_SUBMISSION_SUCCESS = "success";
    private final static String SAVE_SUBMISSION_FAIL = "fail";

    private final ReaderService readerService;
    private final UserService userService;
    private final SearchService searchService;

    public ReaderController(ReaderService readerService, UserService userService, SearchService searchService) {

        this.readerService = readerService;
        this.userService = userService;
        this.searchService = searchService;
    }

    @ModelAttribute("user")
    public User loggedInUser() {

        return userService.getCurrentUser();
    }

    @ModelAttribute("searchedUser")
    public User emptyUser() {

        return new User();
    }

    @GetMapping("reader")
    public String getReaderHome(Model model) {

        User reader = userService.getCurrentUser();

        model.addAttribute("possibleTheses", readerService.getPossibleTheses(reader));
        model.addAttribute("assignedTheses", readerService.getAssignedTheses(reader));
        return "pages/reader";
    }

    @PostMapping("reader/{thesisId}/bid")
    public String bidOnThesis(@PathVariable Long thesisId){

        try {

            readerService.bidOnThesis(thesisId, userService.getCurrentUser());
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/reader?" + SAVE_SUBMISSION_FAIL;
        }

        return "redirect:/reader?" + SAVE_SUBMISSION_SUCCESS;
    }

    @PostMapping("reader/search")
    public String searchThesis(Model model, @ModelAttribute("searchedUser") User user){

        model.addAttribute("searchedThesisList", searchService.searchThesisForReader("%" + user.getFirstName() + "%",
                "%" + user.getLastName() + "%"));
        model.addAttribute("searchedPossibleThesisList", searchService.searchPossibleThesisForReader("%" + user.getFirstName() + "%",
                "%" + user.getLastName() + "%"));

        model.addAttribute("possibleTheses", readerService.getPossibleTheses(userService.getCurrentUser()));
        model.addAttribute("assignedTheses", readerService.getAssignedTheses(userService.getCurrentUser()));

        return "pages/reader";
    }
}