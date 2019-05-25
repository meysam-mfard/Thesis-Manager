package e.group.thesismanager.controller;

import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.service.StudentService;
import e.group.thesismanager.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;
    private final UserService userService;

    public StudentController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User loggedInUser(Model model) {
        return userService.getCurrentUser();
    }

    @RequestMapping("")
    public String getStudentHome(Model model,
                                 @ModelAttribute User student) {

        model.addAttribute("theses", studentService.getTheses(student));

        return "pages/student";
    }

    @GetMapping("thesis")
    public String getThesis(Model model, @RequestParam(name = "stdId") Long studentId ) throws MissingRoleException {
        /*if (!userService.getCurrentUser().getId().equals(studentId))
            //todo:
            return "403error";*/
        model.addAttribute("thesis", studentService.getThesisForActiveSemesterByStudentId(studentId));
        model.addAttribute("studentId", studentId);
        return "pages/thesis";
    }

    @GetMapping("submission")
    public String getSubmissionFrom(Model model, Authentication authentication) {
        model.addAttribute("semesters", studentService.getSemesters()); // todo: filter by "active" semesters?
        model.addAttribute("submissionTypes", Arrays.asList(SubmissionType.values()));

        return "pages/studentSubmissionForm";
    }

    @PostMapping("/submit")
    public String postSubmit(@ModelAttribute User student,
                             @ModelAttribute byte[] file,
                             @ModelAttribute String fileName,
                             @ModelAttribute String fileType,
                             @ModelAttribute Semester semester,
                             @ModelAttribute SubmissionType type) {

        try {
            Thesis thesis = studentService.getThesis(student, semester);

            if(thesis == null) {
                thesis = studentService.initThesis(student, semester);
            }

            Document document = new Document();
            document.setFile(file);
            document.setFileName(fileName);
            document.setFileType(fileType);
            document.setAuthor(student);

            Submission submission = new Submission();
            submission.setType(type);
            submission.setSubmittedDocument(document);

            thesis.addSubmission(submission);

        } catch (MissingRoleException e) {
            return "pages/student-submit?fail";
        }

        return "pages/student-submit?success";
    }

    @GetMapping("/requestSupervisor")
    public String getRequestSupervisor(Model model) {
        model.addAttribute("supervisors", studentService.getSupervisors());
        model.addAttribute("semesters", studentService.getSemesters()); // todo: filter by "active" semesters?

        return "request-supervisor";
    }

    @PostMapping("/requestSupervisor")
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