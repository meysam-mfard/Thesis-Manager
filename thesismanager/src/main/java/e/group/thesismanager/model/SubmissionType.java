package e.group.thesismanager.model;

public enum SubmissionType {

    PROJECT_DESCRIPTION("Project Description"),
    PROJECT_PLAN("Project Plan"),
    REPORT("Report"),
    FINAL_REPORT("Final Report");

    private final String text;

    SubmissionType(String s) {

        text = s;
    }

    @Override
    public String toString() {

        return text;
    }
}