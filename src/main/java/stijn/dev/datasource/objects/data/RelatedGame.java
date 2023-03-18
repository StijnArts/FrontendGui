package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;
import stijn.dev.datasource.objects.items.*;

public class RelatedGame {
    private StringProperty relatedGameEntry;
    private String game;
    private String platform;
    private StringProperty relationship;
    private StringProperty description;
    public RelatedGame(String game, String platform, String relationship, String description){
        this.relatedGameEntry = new SimpleStringProperty("("+platform+") "+game);
        this.game = game;
        this.platform = platform;
        this.relationship =new SimpleStringProperty(relationship);
        this.description = new SimpleStringProperty(description);
    }

    public String getGame() {
        return game;
    }

    public StringProperty getRelatedGameEntry(){
        return relatedGameEntry;
    }

    public void setRelatedGameEntry(StringProperty relatedGameEntry) {
        this.relatedGameEntry = relatedGameEntry;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public StringProperty relationshipProperty() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship.set(relationship);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setGame(String game) {
        this.game = game;
    }

    public StringProperty getRelationship() {
        return relationship;
    }

    public void setRelationship(StringProperty relationship) {
        this.relationship = relationship;
    }

    public StringProperty getDescription() {
        return description;
    }

    public void setDescription(StringProperty description) {
        this.description = description;
    }
}
