package vrqualitycontrol;

public class CheckItem {
    private final int itemId;
    private final HelmetModel model;
    private final String name;

    public CheckItem(int itemId, HelmetModel model, String name) {
        this.itemId = itemId;
        this.model = model;
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public HelmetModel getModel() {
        return model;
    }

    public String getName() {
        return name;
    }
}