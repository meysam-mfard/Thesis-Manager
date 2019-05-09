package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class Thesis extends BaseEntity {

    @OneToOne
    private User student;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private User coordinator;

    @ManyToOne
    private User supervisor;

    @ManyToMany
    private Set<User> readers = new HashSet<>();

    @ManyToMany
    private Set<User> bidders = new HashSet<>();

    @ManyToMany
    private Set<User> opponent = new HashSet<>();

    @OneToMany(mappedBy = "thesis", cascade = CascadeType.ALL)
    private List<Submission> submissions = new LinkedList<>();

    private Float finalGrade;

    private boolean supervisorAccept;

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        submission.setThesis(this);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Thesis)) return false;
        Thesis thesis = (Thesis) o;

        return student.equals(thesis.student) &&
                semester.equals(thesis.semester);
    }

    @Override
    public int hashCode() {

        return Objects.hash(student, semester);
    }
}
