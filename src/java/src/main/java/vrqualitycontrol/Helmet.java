package vrqualitycontrol;

public class Helmet {
    private final String serial;
    private final HelmetModel model;
    private final String batch;

    public Helmet(String serial, HelmetModel model, String batch) {
        this.serial = serial;
        this.model = model;
        this.batch = batch;
    }

    public String getSerial() {
        return serial;
    }

    public HelmetModel getModel() {
        return model;
    }

    public String getBatch() {
        return batch;
    }
}