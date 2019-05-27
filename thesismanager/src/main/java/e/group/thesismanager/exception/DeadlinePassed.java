package e.group.thesismanager.exception;

import e.group.thesismanager.model.SubmissionType;

public class DeadlinePassed extends RuntimeException {

    public DeadlinePassed() {
    }

    public DeadlinePassed(String message) {

        super(message);
    }

    public DeadlinePassed(SubmissionType submissionType) {

        super("Deadline for " + submissionType + " is passed.");
    }

    public DeadlinePassed(String message, Throwable throwable) {

        super(message, throwable);
    }
}
