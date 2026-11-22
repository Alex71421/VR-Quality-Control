package vrqualitycontrol;

import java.util.ArrayList;
import java.util.List;

public class ControlAct {
    private final int actId;
    private final Helmet helmet;
    private final String date;
    private final Employee inspector;
    private final List<CheckResult> results = new ArrayList<>();
    private String decision;

    public ControlAct(int actId, Helmet helmet, String date, Employee inspector, String decision) {
        this.actId = actId;
        this.helmet = helmet;
        this.date = date;
        this.inspector = inspector;
        this.decision = decision;
    }

    public int getActId() {
        return actId;
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public String getDate() {
        return date;
    }

    public Employee getInspector() {
        return inspector;
    }

    public List<CheckResult> getResults() {
        return List.copyOf(results);
    }

    public String getDecision() {
        return decision;
    }

    public boolean addResult(CheckItem item, boolean passed, String defect) {
        int itemId = item.getItemId();
        for (CheckResult result : results) {
            if (result.getItem().getItemId() == itemId) {
                System.out.println("Пункт " + itemId + " уже записан в акте " + actId);
                return false;
            }
        }
        results.add(new CheckResult(item, passed, defect));
        return true;
    }

    private String decide() {
        String result = "годен";
        for (CheckResult checkResult : results) {
            if (!checkResult.isPassed()) {
                result = "брак";
            }
        }
        return result;
    }

    public String closeAct() {
        if (results.isEmpty()) {
            return null;
        }

        decision = decide();
        return decision;
    }
}