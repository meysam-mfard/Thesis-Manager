package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User extends BaseEntity{

    private String firstName;
    private String lastName;
    private Set<String> roles = new HashSet<>();



}
