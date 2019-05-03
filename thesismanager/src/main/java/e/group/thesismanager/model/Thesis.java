package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Thesis extends BaseEntity{

    @OneToOne
    private User student;

    @ManyToOne
    private Semester semester;

    @OneToOne
    private User supervisor;

    @OneToMany
    private Set<User> readers;

    @OneToMany
    private Set<User> bidders;

    @OneToOne
    private User opponent;

    @OneToMany
    private List<Submission> submissions;

    private Float finalGrade;


}
