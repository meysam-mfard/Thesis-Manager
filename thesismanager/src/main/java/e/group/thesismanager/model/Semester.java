package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.Year;

@Data
@Entity
public class Semester extends BaseEntity{

    private SemesterPeriod semesterPeriod;
    private Year year;
    private LocalDateTime projectDescriptionDeadline;
    private LocalDateTime projectPlanDeadline;
    private LocalDateTime reportDeadline;
    private LocalDateTime finalReportDeadline;
    private boolean active = false;
}