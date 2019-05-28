package e.group.thesismanager.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Submission extends BaseEntity {

    @EqualsAndHashCode.Include
    @ManyToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Thesis thesis;

    @EqualsAndHashCode.Include
    @OneToOne (cascade = CascadeType.ALL)
    private Document submittedDocument;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new LinkedList<>();

    private Float grade;

    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private SubmissionType type;

    public void addFeedback(Feedback feedback) {

        feedbacks.add(feedback);
    }

    public String toString() {
        return "Submission(thesis=" + this.getThesis() + ", submittedDocument=" + this.getSubmittedDocument() + ", feedbacks=" + this.getFeedbacks() + ", grade=" + this.getGrade() + ", type=" + this.getType() + ")";
    }

    public Submission() {
    }

    public Submission(Thesis thesis, SubmissionType type) {
        this.thesis = thesis;
        this.type = type;
    }
}
