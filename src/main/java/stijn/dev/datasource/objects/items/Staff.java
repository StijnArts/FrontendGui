package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

public class Staff {
    private StringProperty StaffID;
    private StringProperty FirstName;
    private StringProperty LastName;
    private StringProperty role;

    public Staff(String staffID, String firstName, String lastName, String role) {
        StaffID = new SimpleStringProperty(staffID);
        FirstName = new SimpleStringProperty(firstName);
        LastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
    }

    public Staff(String staffID, String firstName, String lastName) {
        StaffID = new SimpleStringProperty(staffID);
        FirstName = new SimpleStringProperty(firstName);
        LastName = new SimpleStringProperty(lastName);
    }

    public String getStaffID() {
        return StaffID.get();
    }

    public StringProperty staffIDProperty() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        this.StaffID.set(staffID);
    }

    public String getFirstName() {
        return FirstName.get();
    }

    public StringProperty firstNameProperty() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName.set(firstName);
    }

    public String getLastName() {
        return LastName.get();
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public StringProperty lastNameProperty() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName.set(lastName);
    }
}
