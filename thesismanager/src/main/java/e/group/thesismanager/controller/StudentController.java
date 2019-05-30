package e.group.thesismanager.controller;

import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.SubmissionType;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.service.StudentService;
import e.group.thesismanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class StudentController extends AbstractDocumentSubmission{

    private final StudentService studentService;
    private final UserService userService;

    public StudentController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User loggedInUser() {
        return userService.getCurrentUser();
    }

    @GetMapping("student")
    public String getStudentHome(Model model) {

        User student = userService.getCurrentUser();

        model.addAttribute("allowedSubmissionTypes", studentService.getAllowedSubmissionTypes(student.getId()));

        Thesis thesis;

        try {
            thesis = studentService.getThesisByStudentId(student.getId());
        } catch (Exception e) {
            thesis = null;
        }

        model.addAttribute("thesis", thesis);
        model.addAttribute("supervisors", studentService.getSupervisors(thesis));

        return "pages/student";
    }

    @GetMapping({"/student/thesis", "/reader/thesis", "/opponent/thesis", "/supervisor/thesis", "/coordinator/thesis"})
    public String getThesisByStudentID(Model model, @RequestParam(name = "stdId") Long studentId ) {
        model.addAttribute("thesis", studentService.getThesisByStudentId(studentId));
        model.addAttribute("studentId", studentId);
        return "pages/thesis";
    }

    @GetMapping({"/student/thesis/id", "/reader/thesis/id", "/opponent/thesis/id", "/supervisor/thesis/id", "/coordinator/thesis/id"})
    public String getThesisByThesisId(Model model, @RequestParam(name = "thesisId") Long thesisId ) {
        Thesis thesis = studentService.getThesisById(thesisId);
        model.addAttribute("thesis", thesis);
        model.addAttribute("studentId", thesis.getStudent().getId());
        return "pages/thesis";
    }

    @PostMapping("/student/submit")
    public String postSubmit(Model model, @RequestParam(name = "stdId") Long studentId
            , @RequestParam(name = "subType") String submissionTypeStr
            , @RequestParam String comment
            , @RequestParam MultipartFile file) {

        //Validating comment size, file type and file size
        String errorMessage = getCommentErrorMessageIfNotAcceptable(comment);
        if(errorMessage == null)
            errorMessage = getFileErrorMessageIfNotAcceptable(file);
        if( errorMessage != null) {
            log.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "pages/error";
        }

        SubmissionType submissionType = SubmissionType.strToType(submissionTypeStr);
        try {
            studentService.submitDocument(studentId, comment, file, submissionType);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", submissionType.toString()
                    + " submission was not possible.");
            return "pages/error";
        }

        return "redirect:/student";
    }

    @PostMapping("/student/requestSupervisor")
    public String postRequestSupervisor(@RequestParam(name = "thId") Long thesisId,
                                        @RequestParam(name = "supId") Long supervisorId) {

        Thesis thesis = studentService.getThesisById(thesisId);
        try {
            studentService.proposeSupervisor(thesis, userService.getUserById(supervisorId));
        } catch (MissingRoleException | InvalidSupervisorRequestException e) {
            return "redirect:/student?fail";
        }

        return "redirect:/student?success";
    }
}