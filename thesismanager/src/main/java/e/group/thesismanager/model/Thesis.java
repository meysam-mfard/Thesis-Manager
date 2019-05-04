package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Thesis extends BaseEntity{

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

    @OneToMany
    private List<Submission> submissions = new LinkedList<>();

    private Float finalGrade;


}
