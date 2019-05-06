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

    @ManyToOne
    private Thesis thesis;

    @OneToOne
    private Document submittedDocument;

    @OneToMany
    private List<Feedback> feedbacks = new LinkedList<>();

    @ElementCollection
    @MapKeyJoinColumn(name = "grader")
    private Map<User, Float> grades = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private SubmissionType type;

}
