package e.group.thesismanager.model;

public enum SupervisorRequestStatus {

    NOT_ASSIGNED("Not Assigned"),
    REQUEST_SENT("Request Sent"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");

    private final String text;

    SupervisorRequestStatus(String text) {

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static SupervisorRequestStatus fromString(String text) {

        for (SupervisorRequestStatus supervisorRequestStatus : SupervisorRequestStatus.values()) {

            if (supervisorRequestStatus.text.equalsIgnoreCase(text)) {
                return supervisorRequestStatus;
            }
        }
        return null;
    }
}