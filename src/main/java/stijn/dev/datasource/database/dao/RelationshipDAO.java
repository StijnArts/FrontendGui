package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;

import java.util.*;

public class RelationshipDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public List<String> getRelationShips() {
        String characterQuery = "MATCH (r:GameRelationship)" +
                "Return r.Name ORDER BY r.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery));
        ArrayList<String> relatedGames = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            relatedGames.add(String.valueOf(row.get("r.Name")));
        }
        return relatedGames;
    }

    public List<String> getPlatformRelationShips() {
        String characterQuery = "MATCH (r:PlatformRelationship)" +
                "Return r.Name ORDER BY r.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery));
        ArrayList<String> relatedGames = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            relatedGames.add(String.valueOf(row.get("r.Name")));
        }
        return relatedGames;
    }
}
