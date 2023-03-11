package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class TagDAO {

    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createTag(Game game, String tag){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", game.getName());
        parameters.put("platformName",game.getPlatform());
        parameters.put("tag", tag);
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "MERGE (t:Tag {Name:$tag})" +
                "MERGE (g)-[:HAS_TAG]->(t)",
                parameters));
    }
}
