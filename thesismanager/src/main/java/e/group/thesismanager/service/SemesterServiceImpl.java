package e.group.thesismanager.service;

import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.SubmissionType;
import e.group.thesismanager.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService{

    private final SemesterRepository semesterRepository;

    public SemesterServiceImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public List<Semester> getSemesters() {
        return semesterRepository.findAll();
    }

    public Semester getCurrentSemester() {
        return semesterRepository.findByActiveIsTrue();
    }

    public Boolean isDeadlinePassed(SubmissionType submissionType) {
        Boolean isDeadlinePassed = null;
        switch (submissionType) {
            case PROJECT_DESCRIPTION:
                isDeadlinePassed = semesterRepository.findByActiveIsTrue().getProjectDescriptionDeadline().isBefore(LocalDateTime.now());
                break;
            case PROJECT_PLAN:
                isDeadlinePassed = semesterRepository.findByActiveIsTrue().getProjectPlanDeadline().isBefore(LocalDateTime.now());
                break;
            case REPORT:
                isDeadlinePassed = semesterRepository.findByActiveIsTrue().getReportDeadline().isBefore(LocalDateTime.now());
                break;
            case FINAL_REPORT:
                isDeadlinePassed = semesterRepository.findByActiveIsTrue().getFinalReportDeadline().isBefore(LocalDateTime.now());
                break;
        }
        return isDeadlinePassed;
    }
}
