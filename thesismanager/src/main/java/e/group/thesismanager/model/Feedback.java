package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Feedback extends Document {

    private Role authorRole;
}
