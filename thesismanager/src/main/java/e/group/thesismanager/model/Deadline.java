package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@Entity
public class Deadline {
    private LocalDateTime projectDescriptionDeadline;
    private LocalDateTime projectPlanDeadline;
    private LocalDateTime projectReportDeadline;
    private LocalDateTime projectFinalReportDeadline;
}
