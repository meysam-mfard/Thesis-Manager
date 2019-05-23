package e.group.thesismanager.controller;

import e.group.thesismanager.model.Feedback;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ReaderController {

    private static final String SAVE_SUCCESS = "success";
    private static final String SAVE_FAIL = "fail";

    private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping("reader/feedback/{feedbackId}")
    public String giveFeedback(Model model, @PathVariable Long feedbackId, @ModelAttribute("feedback")Feedback feedback){
        try {
            readerService.feedbackOnSubmission(feedbackId, feedback);
            model.addAttribute("feedback",feedback);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/reader?"+SAVE_FAIL;
        }

        return "redirect:/reader?"+SAVE_SUCCESS;
    }

    @PostMapping("reader/{thesisId}/bid")
    public String bidOnThesis(@PathVariable Long thesisId, @ModelAttribute User reader){
        try {
            readerService.bidOnThesis(thesisId,reader);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/reader?"+SAVE_FAIL;
        }

        return "redirect:/reader?"+SAVE_SUCCESS;
    }
    @GetMapping("reader")
    public String getReaderHome(Model model) {

        User reader = readerService.getUserByUsername(getCurrentUsername());
        model.addAttribute("theses", readerService.getTheses());
        return "pages/reader";
    }

    private String getCurrentUsername() {

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return username = ((UserDetails)principal).getUsername();
        } else {
            return username = principal.toString();
        }
    }

}
