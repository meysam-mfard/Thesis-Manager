package e.group.thesismanager.model;

public enum SubmissionType {

    /*IMPORTANT: IF NEW TYPE IS ADDEDE, "strToType" MUST BE UPDATED*/
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

    public static SubmissionType strToType(String str) {
        if (str.equals(PROJECT_DESCRIPTION.text))
            return SubmissionType.PROJECT_DESCRIPTION;
        else if (str.equals(PROJECT_PLAN.text))
            return SubmissionType.PROJECT_PLAN;
        else if (str.equals(REPORT.text))
            return SubmissionType.REPORT;
        else if (str.equals(FINAL_REPORT.text))
            return SubmissionType.FINAL_REPORT;
        return null;
    }
}