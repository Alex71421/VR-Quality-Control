package vrqualitycontrol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonDataLoader {
    public QualityControl load(Path path) throws IOException {
        String json = Files.readString(path);
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        Map<Integer, HelmetModel> models = new LinkedHashMap<>();
        for (JsonElement element : data.getAsJsonArray("models")) {
            JsonObject row = element.getAsJsonObject();
            int modelId = row.get("model_id").getAsInt();
            String name = row.get("name").getAsString();
            models.put(modelId, new HelmetModel(modelId, name));
        }

        Map<Integer, CheckItem> checkItems = new LinkedHashMap<>();
        for (JsonElement element : data.getAsJsonArray("check_items")) {
            JsonObject row = element.getAsJsonObject();
            int itemId = row.get("item_id").getAsInt();
            HelmetModel model = models.get(row.get("model_id").getAsInt());
            String name = row.get("name").getAsString();
            checkItems.put(itemId, new CheckItem(itemId, model, name));
        }

        Map<String, Helmet> helmets = new LinkedHashMap<>();
        for (JsonElement element : data.getAsJsonArray("helmets")) {
            JsonObject row = element.getAsJsonObject();
            String serial = row.get("serial").getAsString();
            HelmetModel model = models.get(row.get("model_id").getAsInt());
            String batch = row.get("batch").getAsString();
            helmets.put(serial, new Helmet(serial, model, batch));
        }

        Map<Integer, Employee> employees = new LinkedHashMap<>();
        for (JsonElement element : data.getAsJsonArray("employees")) {
            JsonObject row = element.getAsJsonObject();
            int employeeId = row.get("employee_id").getAsInt();
            String fullName = row.get("full_name").getAsString();
            employees.put(employeeId, new Employee(employeeId, fullName));
        }

        Map<Integer, ControlAct> actsById = new LinkedHashMap<>();
        for (JsonElement element : data.getAsJsonArray("acts")) {
            JsonObject row = element.getAsJsonObject();
            int actId = row.get("act_id").getAsInt();
            Helmet helmet = helmets.get(row.get("serial").getAsString());
            String date = row.get("date").getAsString();
            Employee inspector = employees.get(row.get("inspector_id").getAsInt());

            String decision = null;
            if (!row.get("decision").isJsonNull()) {
                decision = row.get("decision").getAsString();
            }

            actsById.put(actId, new ControlAct(actId, helmet, date, inspector, decision));
        }

        for (JsonElement element : data.getAsJsonArray("results")) {
            JsonObject row = element.getAsJsonObject();
            ControlAct act = actsById.get(row.get("act_id").getAsInt());
            CheckItem item = checkItems.get(row.get("item_id").getAsInt());
            boolean passed = row.get("passed").getAsBoolean();
            String defect = row.get("defect").getAsString();
            act.addResult(item, passed, defect);
        }

        return new QualityControl(models, checkItems, helmets, employees, new ArrayList<>(actsById.values()));
    }
}