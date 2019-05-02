package e.group.thesismanager.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.File;
import java.time.LocalDateTime;

@Entity
public class Document extends BaseEntity{

    private String comment;
    private File file;

    @ManyToOne
    private User author;

    private LocalDateTime submissionTime;
}
