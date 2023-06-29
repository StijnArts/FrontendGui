package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class PlaymodeDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public void createPlayMode(GameImportItem gameImportItem){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", gameImportItem.getName());
        parameters.put("platformName", gameImportItem.getPlatform());
        parameters.put("name", gameImportItem.getESRBRating());
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "MERGE (m:PlayMode {Name:$name})" +
                "MERGE (g)-[:HAS_MODE]->(m)",
                parameters));
    }

    public void createEdgePlayMode(HashMap<String, Object> parameters){
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "Match (m:PlayMode {Name:$name})" +
                "MERGE (g)-[:HAS_MODE]->(m)",
                parameters));
    }

    public ArrayList<String> getPlayModes() {
        String ESRBQuery = "MATCH (p:PlayMode) RETURN p.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery));
        ArrayList<String> playmodes = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            playmodes.add(String.valueOf(row.get("p.Name")));
        }
        return playmodes;
    }

    public ArrayList<String> getPlayModes(HashMap<String, Object> parameters) {
        String playmodeQuery = "MATCH (p:PlayMode)-[:HAS_MODE]-(g:Game) " +
                "RETURN p.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(playmodeQuery,parameters));
        ArrayList<String> playmodes = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            playmodes.add(String.valueOf(row.get("p.Name")));
        }
        return playmodes;
    }
}
