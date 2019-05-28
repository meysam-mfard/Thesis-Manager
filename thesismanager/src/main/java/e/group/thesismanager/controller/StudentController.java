package e.group.thesismanager.controller;

import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.service.SemesterService;
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
    private final SemesterService semesterService;

    public StudentController(StudentService studentService, UserService userService, SemesterService semesterService) {
        this.studentService = studentService;
        this.userService = userService;
        this.semesterService = semesterService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {
        return userService.getCurrentUser();
    }

    @GetMapping("student")
    public String getStudentHome(Model model) throws MissingRoleException {

        User student = userService.getCurrentUser();

        model.addAttribute("allowedSubmissionTypes", studentService.getAllowedSubmissionTypes(student.getId()));

        Thesis thesis = null;
        try {
            thesis = studentService.getThesisByStudentId(student.getId());
        } catch (Exception e) {
            thesis = null;
        }
        model.addAttribute("thesis", thesis);
        //model.addAttribute("theses", studentService.getTheses(student));

        return "pages/student";
    }

    @GetMapping({"/student/thesis", "/reader/thesis", "/opponent/thesis", "/supervisor/thesis", "/coordinator/thesis"})
    public String getThesis(Model model, @RequestParam(name = "stdId") Long studentId ) {
        model.addAttribute("thesis", studentService.getThesisByStudentId(studentId));
        model.addAttribute("studentId", studentId);
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
            Submission submission = studentService.submitDocument(studentId, comment, file, submissionType);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", submissionType.toString() + " submission was not possible.");
            return "pages/error";
        }

        return "redirect:/student";
    }

    @GetMapping("/student/requestSupervisor")
    public String getRequestSupervisor(Model model) {
        model.addAttribute("supervisors", studentService.getSupervisors());
        model.addAttribute("semesters", semesterService.getSemesters()); // todo: filter by "active" semesters?

        return "request-supervisor";
    }

    @PostMapping("/student/requestSupervisor")
    public String postRequestSupervisor(@ModelAttribute User student,
                                        @ModelAttribute Semester semester,
                                        @ModelAttribute User supervisor) {

        Thesis thesis = studentService.getThesis(student, semester);

        try {
            studentService.proposeSupervisor(thesis, supervisor);
        } catch (MissingRoleException | InvalidSupervisorRequestException e) {
            return "request-supervisor?fail";
        }

        return "request-supervisor?success";
    }
}