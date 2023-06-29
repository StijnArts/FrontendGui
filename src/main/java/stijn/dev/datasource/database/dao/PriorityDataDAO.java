package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class PriorityDataDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public PriorityData getPriority(HashMap<String, Object> parameters) {
        String ESRBQuery = "MATCH (p:Priority)-[:HAS_PRIORITY]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN p.UniqueName, p.Priority";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery,parameters));
        PriorityData priority = null;
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            priority = new PriorityData(String.valueOf(row.get("p.UniqueName")),
                    Integer.valueOf(String.valueOf(row.get("p.Priority"))));
        }
        return priority;
    }

    public ArrayList<PriorityData> getPriorities() {
        String ESRBQuery = "MATCH (p:Priority)" +
                " RETURN p.UniqueName, p.Priority";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery));
        ArrayList<PriorityData> priorities = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            priorities.add(new PriorityData(String.valueOf(row.get("p.UniqueName")),
                    Integer.valueOf(String.valueOf(row.get("p.Priority")))));
        }
        return priorities;
    }
}
