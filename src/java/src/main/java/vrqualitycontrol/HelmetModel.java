package vrqualitycontrol;

public class HelmetModel {
    private final int modelId;
    private final String name;

    public HelmetModel(int modelId, String name) {
        this.modelId = modelId;
        this.name = name;
    }

    public int getModelId() {
        return modelId;
    }

    public String getName() {
        return name;
    }
}