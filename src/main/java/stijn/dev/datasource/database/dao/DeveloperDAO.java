package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class DeveloperDAO {

    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createDeveloper(Game game) {
        if(!game.getDeveloper().equals("N/A")) {
            boolean developerExists = false;
            while(!developerExists) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("developerName", game.getDeveloper().getDeveloperName().trim());
                parameters.put("gameId", game.getGameId());
                parameters.put("gameName", game.getName());
                parameters.put("platformName", game.getPlatform());
                String queryString = "MATCH (d:Publisher {PublisherName:$developerName}), (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "SET d:Developer, d.DeveloperName = $developerName " +
                        "MERGE (d)<-[:MADE_BY]-(game) Return d";
                Result result = neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
                if (!result.hasNext()) {
                    queryString = "Match (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                            "MERGE (n:Developer {DeveloperName:$developerName}) " +
                            "MERGE r = (n)<-[:MADE_BY]-(game) Return r";
                    Query query = new Query(queryString, parameters);
                    neo4JDatabaseHelper.runQuery(query);
                }
                String checkExistsQuery = "MATCH (d:Developer {DeveloperName:$developerName})<-[r:MADE_BY]-(game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName})" +
                        "Return r";
                Query query = new Query(checkExistsQuery, parameters);
                Result exists = neo4JDatabaseHelper.runQuery(query);
                developerExists = exists.hasNext();
            }
        }

    }
}
