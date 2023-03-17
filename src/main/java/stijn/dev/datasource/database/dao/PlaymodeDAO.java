package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;

import java.util.*;

public class PlaymodeDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<String> getPlaymodes() {
        String ESRBQuery = "MATCH (p:Playmode) RETURN p.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery));
        ArrayList<String> playmodes = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            playmodes.add(String.valueOf(row.get("p.Name")));
        }
        return playmodes;
    }

    public ArrayList<String> getPlaymodes(HashMap<String, Object> parameters) {
        String ESRBQuery = "MATCH (p:Playmode)-[:HAS_PLAYMODE]-(g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName})" +
                " RETURN p.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery,parameters));
        ArrayList<String> playmodes = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            playmodes.add(String.valueOf(row.get("p.Name")));
        }
        return playmodes;
    }
}
