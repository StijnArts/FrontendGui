package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;

public class RelatedPlatform {
    private StringProperty platformName;
    private StringProperty id;
    private StringProperty relationship;
    private StringProperty description;
    public RelatedPlatform(String id, String platformName, String relationship, String description){
        this.id = new SimpleStringProperty(id);
        this.platformName = new SimpleStringProperty(platformName);
        this.relationship =new SimpleStringProperty(relationship);
        this.description = new SimpleStringProperty(description);
    }

    public String getPlatformName() {
        return platformName.get();
    }

    public StringProperty platformNameProperty() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName.set(platformName);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getRelationship() {
        return relationship.get();
    }

    public StringProperty relationshipProperty() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship.set(relationship);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
