package vrqualitycontrol;

public class Employee {
    private final int employeeId;
    private final String fullName;

    public Employee(int employeeId, String fullName) {
        this.employeeId = employeeId;
        this.fullName = fullName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFullName() {
        return fullName;
    }
}