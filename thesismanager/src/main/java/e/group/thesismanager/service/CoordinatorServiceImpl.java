package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Submission;
import e.group.thesismanager.model.SubmissionType;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CoordinatorServiceImpl implements CoordinatorService {
    private List<Thesis> thesisList;
    AssessmentService assessmentService;

    @Autowired
    public CoordinatorServiceImpl(AssessmentService assessmentService){
        this.assessmentService = assessmentService;
    }



    @Override
    public Thesis assignOpponent(User student, Set<User> opponent) {
        Thesis thesis = getThesisByStudent(student);
        thesis.setOpponent(opponent);
        return thesis;
    }

    @Override
    public Thesis assignSupervisor(User student, User supervisor) {
        Thesis thesis = getThesisByStudent(student);
        thesis.setSupervisor(supervisor);
        return thesis;

    }

    @Override
    public Thesis evaluateProjectPlan(User student, Float grade) {
        Thesis thesis = getThesisByStudent(student);
        List<Submission> submissions = thesis.getSubmissions();

        Submission evaluatedSubmission = findSubmissionByType(SubmissionType.PROJECT_PLAN, submissions);
        int index = submissions.indexOf(evaluatedSubmission);
        evaluatedSubmission.setGrade(grade);

        submissions.set(index, evaluatedSubmission);
        thesis.setSubmissions(submissions);
        return thesis;
    }



    @Override
    public Thesis gradeFinalProject(User student, Float grade) {
        Thesis thesis = getThesisByStudent(student);
        List<Submission> submissions = thesis.getSubmissions();

        Submission evaluatedSubmission = findSubmissionByType(SubmissionType.FINAL_REPORT, submissions);
        int index = submissions.indexOf(evaluatedSubmission);
        evaluatedSubmission.setGrade(grade);

        submissions.set(index, evaluatedSubmission);
        thesis.setSubmissions(submissions);
        return thesis;
    }

    @Override
    public Thesis initiateThesis(User student) {
        Thesis thesis = new Thesis();
        thesis.setStudent(student);
        return thesis;
    }

    private Submission findSubmissionByType(SubmissionType type, List<Submission> submissions  ){
        Submission submissionToReturn = submissions.stream()
                .filter(s -> s.getType().equals(type))
                .findFirst()
                .orElseThrow(
                        ()-> new NotFoundException("Submission not found"));

        return submissionToReturn;
    }
    @Override
    public void setThesis(List<Thesis> thesisList){
        this.thesisList = thesisList;
    }

    private Thesis getThesisByStudent(User student){
        this.thesisList = assessmentService.getThesis();
        return thesisList.stream()
                .filter(x -> x.getStudent().equals(student))
                .findFirst()
                .orElseThrow( ()-> new NotFoundException("User does not exist." + student.getFirstName() + student.getLastName() ) );
    }

}
