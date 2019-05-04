package e.group.thesismanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@Entity
public class User extends BaseEntity{

    private String firstName;
    private String lastName;

    @ElementCollection(fetch=EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();



}
