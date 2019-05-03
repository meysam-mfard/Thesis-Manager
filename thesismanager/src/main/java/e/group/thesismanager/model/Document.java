package e.group.thesismanager.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.io.File;
import java.time.LocalDateTime;

@Data
@Entity
public class Document extends BaseEntity{

    private String comment;

    @Lob
    private File file;

    @ManyToOne
    private User author;

    private LocalDateTime submissionTime;
}
