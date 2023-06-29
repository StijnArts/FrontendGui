package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class AdditionalAppDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<AdditionalApp> getAdditionalApps(HashMap<String, Object> parameters) {
        String characterQuery = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[a:ADDITIONAL_APP]-(g) " +
                "RETURN a.Path, a.Name, a.Arguments";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery,parameters));
        ArrayList<AdditionalApp> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new AdditionalApp(String.valueOf(row.get("a.Path")),String.valueOf(row.get("a.Name")),
                    String.valueOf(row.get("a.Arguments"))));
        }
        return staff;
    }

    public void createAdditionalApp(HashMap<String, Object> parameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "CREATE (g)-[:ADDITIONAL_APP {Path:$path, Name:$additionalAppName, Arguments:$arguments}]->(g)";
        /*System.out.println("MATCH (g:Game {GameName: \""+parameters.get("gameName")+"\"})-[:ON_PLATFORM]-(p:Platform {PlatformName: \""+parameters.get("platformName")+"\"}) \n" +
                "                WITH g \n" +
                "                MATCH (e:Rating {Rating: \""+parameters.get("rating")+"\", Organization: \""+parameters.get("organization")+"\"}) \n" +
                "                MERGE (g)-[:HAS_RATING]->(e)");*/
        neo4JDatabaseHelper.runQuery(new Query(query, parameters));
    }
}
