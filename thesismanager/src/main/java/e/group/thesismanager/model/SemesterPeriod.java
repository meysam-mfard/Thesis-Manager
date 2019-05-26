package e.group.thesismanager.model;

public enum SemesterPeriod {

    AUTUMN("Autumn"),
    SPRING("Spring");

    private final String text;

    SemesterPeriod(String text) {

        this.text = text;
    }

    @Override
    public String toString() {

        return text;
    }

    public static SemesterPeriod fromString(String text) {

        for (SemesterPeriod semesterPeriod : SemesterPeriod.values()) {

            if (semesterPeriod.text.equalsIgnoreCase(text)) {
                return semesterPeriod;
            }
        }
        return null;
    }
}