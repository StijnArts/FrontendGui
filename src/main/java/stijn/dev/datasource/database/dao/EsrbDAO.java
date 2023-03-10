package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class EsrbDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public void createESRBRating(Game game){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", game.getName());
        parameters.put("platformName",game.getPlatform());
        parameters.put("rating", game.getESRBRating());
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "MERGE (e:ESRBRating {Rating:$rating})" +
                "MERGE (g)-[:HAS_RATING]->(e)",
                parameters));
    }
}
