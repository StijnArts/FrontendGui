package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

public class Character {
    private StringProperty characterID;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty role;
    private StringProperty voiceActor;

    public Character(String characterID, String firstName, String lastName) {
        this.characterID = new SimpleStringProperty(characterID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
    }

    public Character(String characterID, String firstName, String lastName, String role, String voiceActor) {
        this.characterID = new SimpleStringProperty(characterID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.voiceActor = new SimpleStringProperty(voiceActor);
    }

    public String getCharacterID() {
        return characterID.get();
    }

    public String getVoiceActor() {
        return voiceActor.get();
    }

    public StringProperty voiceActorProperty() {
        return voiceActor;
    }

    public void setVoiceActor(String voiceActor) {
        this.voiceActor.set(voiceActor);
    }

    public StringProperty characterIDProperty() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID.set(characterID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
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
}
