package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
public class Submission extends BaseEntity {

    @ManyToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Thesis thesis;

    @OneToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Document submittedDocument;

    @OneToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Feedback> feedbacks = new LinkedList<>();

    private Float grade;

    @Enumerated(EnumType.STRING)
    private SubmissionType type;

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
    }

}
