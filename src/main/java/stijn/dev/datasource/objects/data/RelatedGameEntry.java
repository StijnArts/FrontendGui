package stijn.dev.datasource.objects.data;

import javafx.beans.property.*;

public class RelatedGameEntry {
    private String game;
    private String platform;
    public RelatedGameEntry(String game, String platform) {
        this.game = game;
        this.platform = platform;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "(" + platform + ") " + game;
    }

    public SimpleStringProperty toStringProperty() {
        return new SimpleStringProperty("(" + platform + ") " + game);
    }
}
