package e.group.thesismanager.service;

import e.group.thesismanager.model.Semester;
import e.group.thesismanager.model.SubmissionType;

import java.util.List;

public interface SemesterService {

    List<Semester> getSemesters();

    Semester getCurrentSemester();

    Boolean isDeadlinePassed(SubmissionType submissionType);
}
