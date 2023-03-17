package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class GameImportItemDAO {
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createInDatabaseGame(GameImportItem gameImportItem){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", gameImportItem.getName());
        parameters.put("gameId", gameImportItem.getGameId());
        parameters.put("gamePlatform", gameImportItem.getPlatform());
        String queryString = "MATCH (p:Platform {PlatformName:$gamePlatform}) " +
                "MERGE r = (g:Game {GameName:$gameName, " +
                "GameId:$gameId, Description:$description, CommunityRating:$communityRating, " +
                "CommunityRatingCount:$communityRatingCount, MaxPlayers:$MaxPlayers, DefaultSortingTitle:$gameName}) " +
                "SET g.GamePath = $gamePath " +
                "MERGE (g)-[:ON_PLATFORM]->(p) Return r";
        if(gameFileAlreadyImportedForPlatform(parameters)){
            queryString = "MATCH (p:Platform {PlatformName:$gamePlatform}) " +
                    "CREATE r = (g:Game {GameName:$gameName, GamePath:$gamePath, " +
                    "GameId:$gameId, Description:$description, CommunityRating:$communityRating, " +
                    "CommunityRatingCount:$communityRatingCount, MaxPlayers:$MaxPlayers, DefaultSortingTitle:$gameName}) " +
                    "MERGE (g)-[:ON_PLATFORM]->(p) Return r";
        }
        parameters.put("description", gameImportItem.getDescription());
        parameters.put("gamePath", gameImportItem.getPath());
        parameters.put("communityRating", gameImportItem.getCommunityRating());
        parameters.put("communityRatingCount", gameImportItem.getCommunityRatingCount());
        parameters.put("MaxPlayers", gameImportItem.getMaxPlayers());
        Query query = new Query(queryString, parameters);
        neo4JDatabaseHelper.runQuery(query);
        territoryDAO.createReleaseDates(gameImportItem);
    }

    private boolean gameFileAlreadyImportedForPlatform(HashMap<String, Object> parameters) {
        return !neo4JDatabaseHelper.runQuery(new Query(
                "Match (g:Game {GameName:$gameName, GameId:$gameId}), (p:Platform {PlatformName:$gamePlatform})" +
                        "MATCH (g)-[r:ON_PLATFORM]->(p) RETURN r", parameters)).hasNext();
    }
}
