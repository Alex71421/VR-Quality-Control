package vrqualitycontrol;

import java.util.List;
import java.util.Map;

public class QualityControl {
    private final Map<Integer, HelmetModel> models;
    private final Map<Integer, CheckItem> checkItems;
    private final Map<String, Helmet> helmets;
    private final Map<Integer, Employee> employees;
    private final List<ControlAct> acts;

    public QualityControl(Map<Integer, HelmetModel> models, Map<Integer, CheckItem> checkItems,
                          Map<String, Helmet> helmets, Map<Integer, Employee> employees,
                          List<ControlAct> acts) {
        this.models = models;
        this.checkItems = checkItems;
        this.helmets = helmets;
        this.employees = employees;
        this.acts = acts;
    }

    public boolean registerHelmet(String serial, int modelId, String batch) {
        if (helmets.containsKey(serial)) {
            System.out.println("Шлем " + serial + " уже зарегистрирован");
            return false;
        }
        HelmetModel model = models.get(modelId);
        helmets.put(serial, new Helmet(serial, model, batch));
        return true;
    }

    public ControlAct createAct(String serial, String date, String inspectorName) {
        int maxId = 0;
        for (ControlAct existingAct : acts) {
            maxId = Math.max(maxId, existingAct.getActId());
        }
        int actId = maxId + 1;
        Helmet helmet = helmets.get(serial);
        Employee inspector = getOrAddEmployee(inspectorName);
        ControlAct act = new ControlAct(actId, helmet, date, inspector, null);
        acts.add(act);
        return act;
    }

    private Employee getOrAddEmployee(String fullName) {
        int maxId = 0;
        for (Employee employee : employees.values()) {
            if (employee.getFullName().equals(fullName)) {
                return employee;
            }
            maxId = Math.max(maxId, employee.getEmployeeId());
        }

        int employeeId = maxId + 1;
        Employee employee = new Employee(employeeId, fullName);
        employees.put(employeeId, employee);
        return employee;
    }

    public CheckItem getCheckItem(int itemId) {
        return checkItems.get(itemId);
    }

    public List<HelmetModel> getModels() {
        return List.copyOf(models.values());
    }

    public List<ControlAct> getActs() {
        return List.copyOf(acts);
    }
}