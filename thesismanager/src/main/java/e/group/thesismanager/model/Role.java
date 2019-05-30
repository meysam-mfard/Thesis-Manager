package e.group.thesismanager.model;

public enum Role {

    ROLE_ADMIN("Admin"),
    ROLE_STUDENT("Student"),
    ROLE_COORDINATOR("Coordinator"),
    ROLE_SUPERVISOR("Supervisor"),
    ROLE_READER("Reader"),
    ROLE_OPPONENT("Opponent");

    private final String text;

    Role(String text) {

        this.text = text;
    }

    public String getText() {

        return this.text;
    }

    public static Role fromString(String text) {

        for (Role role : Role.values()) {

            if (role.text.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return null;
    }
}