package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class AlternateNameDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public ArrayList<AlternateName> getAlternateNames(HashMap<String, Object> parameters) {
        String communityAlternateNamesQuery = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[a:ALTERNATE_NAME]-(g) " +
                "RETURN a.AlternateNameID, a.Name";
        Result communityAlternateNamesResult = neo4JDatabaseHelper.runQuery(new Query(communityAlternateNamesQuery,parameters));
        ArrayList<AlternateName> alternateNames = new ArrayList<>();
        while(communityAlternateNamesResult.hasNext()) {
            Map<String, Object> row = communityAlternateNamesResult.next().asMap();
            alternateNames.add(new AlternateName(String.valueOf(row.get("a.AlternateNameID")),String.valueOf(row.get("a.Name")),
                    "Community"));
        }
        String regionAlternateNamesQuery = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[a:ALTERNATE_NAME]-(t:Territory) " +
                "RETURN a.AlternateNameID, a.Name, t.Name";
        Result regionAlternateNamesResult = neo4JDatabaseHelper.runQuery(new Query(regionAlternateNamesQuery,parameters));
        while(regionAlternateNamesResult.hasNext()) {
            Map<String, Object> row = regionAlternateNamesResult.next().asMap();
            alternateNames.add(new AlternateName(String.valueOf(row.get("a.AlternateNameID")),String.valueOf(row.get("a.Name")),
                    String.valueOf(row.get("t.Name"))));
        }
        return alternateNames;
    }

    public void createAlternateName(HashMap<String, Object> alternateNameParameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "CREATE (g)-[:ALTERNATE_NAME {AlternateNameID:$alternateNameId, Name:$alternateName}]->(g)";
        /*System.out.println("MATCH (g:Game {GameName: \""+parameters.get("gameName")+"\"})-[:ON_PLATFORM]-(p:Platform {PlatformName: \""+parameters.get("platformName")+"\"}) \n" +
                "                WITH g \n" +
                "                MATCH (e:Rating {Rating: \""+parameters.get("rating")+"\", Organization: \""+parameters.get("organization")+"\"}) \n" +
                "                MERGE (g)-[:HAS_RATING]->(e)");*/
        neo4JDatabaseHelper.runQuery(new Query(query, alternateNameParameters));
    }

    public void createAlternateNameWithRegion(HashMap<String, Object> alternateNameParameters) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MERGE (t:Territory {Name:$region}) " +
                "CREATE (g)-[:ALTERNATE_NAME {AlternateNameID:$alternateNameId, Name:$alternateName}]->(t)";
        /*System.out.println("MATCH (g:Game {GameName: \""+parameters.get("gameName")+"\"})-[:ON_PLATFORM]-(p:Platform {PlatformName: \""+parameters.get("platformName")+"\"}) \n" +
                "                WITH g \n" +
                "                MATCH (e:Rating {Rating: \""+parameters.get("rating")+"\", Organization: \""+parameters.get("organization")+"\"}) \n" +
                "                MERGE (g)-[:HAS_RATING]->(e)");*/
        neo4JDatabaseHelper.runQuery(new Query(query, alternateNameParameters));
    }
}
