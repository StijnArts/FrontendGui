package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;

import java.util.*;

public class MediaDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public List<String> getPlatformMedia(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String triviaQuery = "MATCH (m:Media)-[:USES]-(platform:Platform) " +
                "WHERE ID(platform) = $id " +
                "RETURN m.MediaSpecification ORDER BY m.MediaSpecification";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<String> manufacturers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            manufacturers.add(String.valueOf(row.get("m.MediaSpecification")));
        }
        return manufacturers;
    }

    public List<String> getMedia() {
        String triviaQuery = "MATCH (m:Media) " +
                "RETURN m.MediaSpecification ORDER BY m.MediaSpecification";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        ArrayList<String> media = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            media.add(String.valueOf(row.get("m.MediaSpecification")));
        }
        return media;
    }
}
