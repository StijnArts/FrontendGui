package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class RelatedGameDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public ArrayList<RelatedGame> getRelatedGames(HashMap<String, Object> parameters) {
        String characterQuery = "MATCH (o:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(o)-[r:RELATED_TO]-(g:Game) Return g.GameName, g.GameId, g.Description, g.Theme, " +
        "g.DefaultSummary, g.DefaultSortingName, g.LaunchParameters, g.GamePath, g.CommunityRating, g.CommunityRatingCount, " +
                "g.MaxPlayers, g.HLTBStory, g.HLTBStoryAndExtra, g.HLTBCompletionist, g.Cooperative, p.PlatformName, r.RelationType, r.Description";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery,parameters));
        ArrayList<RelatedGame> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new RelatedGame(createGameFromDatabase(row), String.valueOf(row.get("r.RelationType")), String.valueOf(row.get("r.Description"))));
        }
        return staff;
    }

    private Game createGameFromDatabase(Map<String, Object> row){
        return new Game(String.valueOf(row.get("g.GameName")), String.valueOf(row.get("g.GamePath")), String.valueOf(row.get("g.GameId")),
                String.valueOf(row.get("g.Description")), String.valueOf(row.get("g.MaxPlayers")),
                String.valueOf(row.get("p.PlatformName")), String.valueOf(row.get("g.CommunityRating")), String.valueOf(row.get("g.CommunityRatingCount")),
                Boolean.valueOf(String.valueOf(row.get("g.Cooperative"))));
    }
}
