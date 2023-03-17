package stijn.dev.datasource.objects.data;

import stijn.dev.datasource.objects.items.*;

public class RelatedGame {
    private Game game;
    private String relationship;
    private String description;
    public RelatedGame(Game game, String relationship, String description){
        this.game = game;
        this.relationship = relationship;
        this.description = description;
    }
}
