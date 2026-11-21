package vrqualitycontrol;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Передайте путь к data.json.");
            return;
        }

        JsonDataLoader loader = new JsonDataLoader();
        QualityControl control = loader.load(Path.of(args[0]));

        control.registerHelmet("VR1-0003", 1, "P-12");
        ControlAct act = control.createAct("VR1-0003", "2026-10-25", "Петров И. С.");
        act.addResult(control.getCheckItem(1), true, "");
        act.addResult(control.getCheckItem(2), true, "");
        act.addResult(control.getCheckItem(3), true, "");
        act.addResult(control.getCheckItem(4), false, "Нечёткость изображения по краям");
        act.closeAct();

        Report protocol = new InspectionProtocol(act);
        protocol.print();

        System.out.println();
        Report defects = new DefectReport(control.getModels(), control.getActs(), "2026-10-20", "2026-10-31");
        defects.print();
    }
}