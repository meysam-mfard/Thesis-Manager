package e.group.thesismanager.model;

public enum SemesterPeriod {
    AUTUMN("Autumn"),
    SPRING("Spring");

    private String str;

    private SemesterPeriod(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}