package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class GameDAO {

    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createInDatabaseGame(Game game){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName",game.getName());
        parameters.put("gameId",game.getGameId());
        parameters.put("gamePlatform",game.getPlatform());
        String queryString = "MATCH (p:Platform {PlatformName:$gamePlatform}) " +
                "MERGE r = (g:Game {GameName:$gameName, " +
                "GameId:$gameId, Description:$description, CommunityRating:$communityRating, " +
                "CommunityRatingCount:$communityRatingCount})" +
                " SET g.GamePath =$gamePath " +
                "MERGE (g)-[:ON_PLATFORM]->(p) Return r";
        if(gameFileAlreadyImportedForPlatform(parameters)){
            queryString = "MATCH (p:Platform {PlatformName:$gamePlatform}) " +
                    "CREATE r = (g:Game {GameName:$gameName, GamePath:$gamePath, " +
                    "GameId:$gameId, Description:$description, CommunityRating:$communityRating, " +
                    "CommunityRatingCount:$communityRatingCount}) " +
                    "MERGE (g)-[:ON_PLATFORM]->(p) Return r";
        }
        parameters.put("description",game.getDescription());
        parameters.put("gamePath",game.getPath());
        parameters.put("communityRating",game.getCommunityRating());
        parameters.put("communityRatingCount",game.getCommunityRatingCount());
        Query query = new Query(queryString, parameters);
        neo4JDatabaseHelper.runQuery(query);
        territoryDAO.createReleaseDates(game);
    }

    private boolean gameFileAlreadyImportedForPlatform(HashMap<String, Object> parameters) {
        return !neo4JDatabaseHelper.runQuery(new Query(
                "Match (g:Game {GameName:$gameName, GameId:$gameId}), (p:Platform {PlatformName:$gamePlatform})" +
                        "MATCH (g)-[r:ON_PLATFORM]->(p) RETURN r", parameters)).hasNext();
    }

    public ArrayList<Game> getGames(){
        String query = "Match (g:Game) Return g.GameName";
        Result result = neo4JDatabaseHelper.runQuery(query);
        ArrayList<Game> games = new ArrayList<>();
        while(result.hasNext()){
            String gameData = "";
            Map<String, Object> row = result.next().asMap();
            for(Map.Entry<String,Object> column:row.entrySet()){
                gameData+= column.getKey()+": "+column.getValue()+". \n";
            }
            gameData+="\n";
            System.out.println(gameData);
        }
        return null;
    }

    private Game createGameFromDatabase(){
        return null;
    }
}
