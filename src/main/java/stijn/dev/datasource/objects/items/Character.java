package stijn.dev.datasource.objects.items;

import javafx.beans.property.*;

import java.util.*;

public class Character {
    private StringProperty characterID;
    private StringProperty name;
    private StringProperty role;
    private StringProperty voiceActor;

    public Character(String characterID, String name) {
        this.characterID = new SimpleStringProperty(characterID);
        this.name = new SimpleStringProperty(name);
    }

    public Character(String characterID, String name, String role, String voiceActor) {
        this.characterID = new SimpleStringProperty(characterID);
        this.role = new SimpleStringProperty(role);
        this.name = new SimpleStringProperty(name);
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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
