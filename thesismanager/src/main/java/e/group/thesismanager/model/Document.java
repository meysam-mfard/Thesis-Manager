package e.group.thesismanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.File;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Document extends BaseEntity {

    protected String comment;

    @Lob
    protected File file;

    @ManyToOne
    protected User author;

    protected LocalDateTime submissionTime;
}