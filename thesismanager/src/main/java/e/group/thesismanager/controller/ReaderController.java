package e.group.thesismanager.controller;

import e.group.thesismanager.model.Feedback;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("reader/feedback/{{feedbackId}")
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

    @PostMapping("reader/bid/{BidId}")
    public String bidOnThesis(Model model, @PathVariable Long BidId, @ModelAttribute("user") User reader, @ModelAttribute("bid") Double bid){
        try {
            readerService.bidOnThesis(BidId,reader);
            model.addAttribute("bid",bid);
        } catch (Exception e) {
            //todo: replace with validator
            e.printStackTrace();
            return "redirect:/reader?"+SAVE_FAIL;
        }

        return "redirect:/reader?"+SAVE_SUCCESS;
    }

}
