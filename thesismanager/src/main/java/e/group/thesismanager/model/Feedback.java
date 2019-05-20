package e.group.thesismanager.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Feedback extends Document {

    private Role authorRole;

    @Builder
    public Feedback(String comment, Byte[] file, User author, LocalDateTime submissionTime, Role authorRole) {

        super(comment, file, author, submissionTime);
        this.authorRole = authorRole;
    }
}