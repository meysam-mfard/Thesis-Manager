package e.group.thesismanager.controller;

import e.group.thesismanager.exception.InvalidSupervisorRequestException;
import e.group.thesismanager.exception.MissingRoleException;
import e.group.thesismanager.model.*;
import e.group.thesismanager.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("student")
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("")
    public String getStudentHome(Model model,
                                 @ModelAttribute User student) {

        model.addAttribute("theses", studentService.getTheses(student));

        return "pages/student";
    }

    @GetMapping("/submit")
    public String getSubmit(Model model) {
        model.addAttribute("semesters", studentService.getSemesters()); // todo: filter by "active" semesters?
        model.addAttribute("submissionTypes", Arrays.asList(SubmissionType.values()));

        return "pages/student-submit";
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