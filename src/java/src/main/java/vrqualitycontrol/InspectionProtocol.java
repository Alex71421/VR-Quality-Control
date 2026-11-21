package vrqualitycontrol;

public class InspectionProtocol extends TextReport implements Report {
    private final ControlAct act;

    public InspectionProtocol(ControlAct act) {
        super("Протокол контроля, акт " + act.getActId());
        this.act = act;
    }

    @Override
    protected void printBody() {
        System.out.println("Шлем: " + act.getHelmet().getSerial()
                + "  дата:  " + act.getDate()
                + "  контролер: " + act.getInspector().getFullName());

        for (CheckResult result : act.getResults()) {
            String mark = "пройден";
            if (!result.isPassed()) {
                mark = "Не пройден: " + result.getDefect();
            }
            System.out.println(result.getItem().getName() + " - " + mark);
        }

        System.out.println("Решение: " + act.getDecision());
    }
}