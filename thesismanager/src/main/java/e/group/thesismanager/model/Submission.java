package e.group.thesismanager.model;

import javax.persistence.OneToOne;
import java.util.Map;

public class Submission extends BaseEntity {

    @OneToOne
    private Document submittedDocument;
    private Map<Role, Document> feedbacks;
    private Map<User, Float> grades;
    private DocumentType documentType;

}
