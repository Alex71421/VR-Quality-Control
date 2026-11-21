package vrqualitycontrol;


public abstract class TextReport {
    private final String title;

    protected TextReport(String title) {
        this.title = title;
    }

    public void print() {
        System.out.println(title);
        printBody();
    }

    protected abstract void printBody();
}