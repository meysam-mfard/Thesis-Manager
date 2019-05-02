package e.group.thesismanager.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Deadline {
    private LocalDateTime projectDescriptionDeadline;
    private LocalDateTime projectPlanDeadline;
    private LocalDateTime projectReportDeadline;
    private LocalDateTime projectFinalReportDeadline;
}
