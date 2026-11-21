package vrqualitycontrol;

import java.util.List;

public class DefectReport extends TextReport implements Report {
    private final List<HelmetModel> models;
    private final List<ControlAct> acts;
    private final String dateFrom;
    private final String dateTo;

    public DefectReport(List<HelmetModel> models, List<ControlAct> acts, String dateFrom, String dateTo) {
        super("Брак по моделям за " + dateFrom + " - " + dateTo + ":");
        this.models = models;
        this.acts = acts;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    protected void printBody() {
        for (HelmetModel model : models) {
            int defects = 0;
            for (ControlAct act : acts) {
                int modelId = act.getHelmet().getModel().getModelId();
                String date = act.getDate();
                if (modelId == model.getModelId()
                        && date.compareTo(dateFrom) >= 0
                        && date.compareTo(dateTo) <= 0
                        && "брак".equals(act.getDecision())) {
                    defects++;
                }
            }
            System.out.println("  " + model.getName() + " - " + defects);
        }
    }
}