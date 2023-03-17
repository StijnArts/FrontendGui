package stijn.dev.datasource.objects.items;

public class Staff {
    private String StaffID;
    private String FirstName;
    private String LastName;

    public Staff(String staffID, String firstName, String lastName) {
        StaffID = staffID;
        FirstName = firstName;
        LastName = lastName;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
