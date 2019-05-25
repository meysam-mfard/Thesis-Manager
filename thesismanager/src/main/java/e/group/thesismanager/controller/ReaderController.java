package e.group.thesismanager.controller;

import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.security.CustomUserDetailsService;
import e.group.thesismanager.service.ReaderService;
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
    private final CustomUserDetailsService customUserDetailsService;

    public ReaderController(ReaderService readerService, CustomUserDetailsService customUserDetailsService) {

        this.readerService = readerService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {

        return customUserDetailsService.getCurrentUser();
    }

    @GetMapping("reader")
    public String getReaderHome(Model model) {

        User reader = customUserDetailsService.getCurrentUser();

        model.addAttribute("possibleTheses", readerService.getPossibleTheses(reader));
        model.addAttribute("assignedTheses", readerService.getAssignedTheses(reader));
        return "pages/reader";
    }

    @PostMapping("reader/{thesisId}/bid")
    public String bidOnThesis(@PathVariable Long thesisId){

        try {

            readerService.bidOnThesis(thesisId, customUserDetailsService.getCurrentUser());
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/reader?" + SAVE_SUBMISSION_FAIL;
        }

        return "redirect:/reader?" + SAVE_SUBMISSION_SUCCESS;
    }
}