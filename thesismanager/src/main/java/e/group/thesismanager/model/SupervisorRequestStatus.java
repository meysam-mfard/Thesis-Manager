package e.group.thesismanager.model;

public enum SupervisorRequestStatus {

    NOT_ASSIGNED(0),
    REQUEST_SENT(1),
    REJECTED(2),
    ACCEPTED(3);

    private int code;

    SupervisorRequestStatus(int code) {

        this.code = code;
    }
}