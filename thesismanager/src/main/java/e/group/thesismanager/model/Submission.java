package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class Submission extends BaseEntity {

    @OneToOne
    private Document submittedDocument;

    @OneToMany
    private List<Feedback> feedbacks;

    @ElementCollection
    private Map<User, Float> grades;

    @Enumerated(EnumType.STRING)
    private SubmissionType type;

}
