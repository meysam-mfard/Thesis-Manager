package e.group.thesismanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Document extends BaseEntity {

    @Lob
    protected String comment;

    @ToString.Exclude
    @Lob
    protected byte[] file;

    protected String fileName;
    protected String fileType;

    @ManyToOne
    protected User author;

    protected LocalDateTime submissionTime;

    @PrePersist
    private void setUpdateTime() {
        if (submissionTime == null)
            submissionTime = LocalDateTime.now();
    }
}