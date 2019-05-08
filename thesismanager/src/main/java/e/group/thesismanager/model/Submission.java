package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class Submission extends BaseEntity {

    @ManyToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Thesis thesis;

    @OneToOne
    private Document submittedDocument;

    @OneToMany
    private List<Feedback> feedbacks = new LinkedList<>();

    private Float grade;

    @Enumerated(EnumType.STRING)
    private SubmissionType type;

}
