package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class RatingDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public void createRating(GameImportItem gameImportItem){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", gameImportItem.getName());
        parameters.put("platformName", gameImportItem.getPlatform());
        parameters.put("rating", gameImportItem.getESRBRating());
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "MERGE (e:Rating {Organization:'ESRB', Rating:$rating}) " +
                "MERGE (g)-[:HAS_RATING]->(e)",
                parameters));
    }

    public void createRating(HashMap<String, Object> parameters){
        neo4JDatabaseHelper.runQuery(new Query("MATCH (g:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "MERGE (e:Rating {Rating:$rating, Organization:$organization}) " +
                "MERGE (g)-[:HAS_RATING]->(e)",
                parameters));
    }

    public ArrayList<String> getRatings(HashMap<String, Object> parameters) {
        String ESRBQuery = "MATCH (e:Rating)-[:HAS_RATING]-(g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName})" +
                " RETURN e.Rating, e.Organization";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery,parameters));
        ArrayList<String> ratings = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            ratings.add(row.get("e.Organization")+": "+row.get("e.Rating"));
        }
        return ratings;
    }

    public ArrayList<String> getRatings() {
        String ESRBQuery = "MATCH (e:Rating) RETURN e.Rating, e.Organization";
        Result result = neo4JDatabaseHelper.runQuery(new Query(ESRBQuery));
        ArrayList<String> ratings = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            ratings.add(row.get("e.Organization")+": "+row.get("e.Rating"));
        }
        return ratings;
    }
}
