package stijn.dev.datasource.objects.items;

public class Character {
    private String characterID;
    private String firstName;
    private String lastName;

    public Character(String characterID, String firstName, String lastName) {
        this.characterID = characterID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
