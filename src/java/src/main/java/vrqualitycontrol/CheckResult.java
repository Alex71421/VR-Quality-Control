package vrqualitycontrol;

public class CheckResult {
    private final CheckItem item;
    private final boolean passed;
    private final String defect;

    public CheckResult(CheckItem item, boolean passed, String defect) {
        this.item = item;
        this.passed = passed;
        this.defect = defect;
    }

    public CheckItem getItem() {
        return item;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getDefect() {
        return defect;
    }
}