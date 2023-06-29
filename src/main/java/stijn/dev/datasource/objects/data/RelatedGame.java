package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;

public class RelatedGame {
    private StringProperty relatedGameEntry;
    private StringProperty game;
    private StringProperty id;
    private StringProperty platform;
    private StringProperty relationship;
    private StringProperty description;
    public RelatedGame(String id, String game, String platform, String relationship, String description){
        this.id = new SimpleStringProperty(id);
        this.relatedGameEntry = new SimpleStringProperty("("+platform+") "+game);
        this.game = new SimpleStringProperty(game);
        this.platform = new SimpleStringProperty(platform);
        this.relationship =new SimpleStringProperty(relationship);
        this.description = new SimpleStringProperty(description);
    }

    public StringProperty getGame() {
        return game;
    }

    public StringProperty getRelatedGameEntry(){
        return relatedGameEntry;
    }

    public StringProperty getId() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setRelatedGameEntry(StringProperty relatedGameEntry) {
        this.relatedGameEntry = relatedGameEntry;
    }

    public StringProperty getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform.set(platform);
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
        this.game.set(game);
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
