package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class DeveloperDAO {

    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createDeveloper(GameImportItem gameImportItem) {
        if(!gameImportItem.getDeveloper().equals("N/A")) {
            boolean developerExists = false;
            while(!developerExists) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("developerName", gameImportItem.getDeveloper().trim());
                parameters.put("gameId", gameImportItem.getGameId());
                parameters.put("gameName", gameImportItem.getName());
                parameters.put("platformName", gameImportItem.getPlatform());
                String queryString = "MATCH (d:Publisher {PublisherName:$developerName}), (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "SET d:Developer, d.DeveloperName = $developerName " +
                        "MERGE (d)<-[:MADE_BY]-(game) Return d";
                Result result = neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
                if (!result.hasNext()) {
                    queryString = "Match (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                            "MERGE (n:Developer {DeveloperName:$developerName}) " +
                            "MERGE r = (n)<-[:MADE_BY]-(game) Return r";
                    Query query = new Query(queryString, parameters);
                    neo4JDatabaseHelper.runQuery(query);
                }
                String checkExistsQuery = "MATCH (d:Developer {DeveloperName:$developerName})<-[r:MADE_BY]-(game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName})" +
                        "Return r";
                Query query = new Query(checkExistsQuery, parameters);
                Result exists = neo4JDatabaseHelper.runQuery(query);
                developerExists = exists.hasNext();
            }
        }
    }

    public void createDeveloper(HashMap<String, Object> parameters) {
        boolean developerExists = false;
        while(!developerExists) {
            String queryString = "MATCH (d:Publisher {PublisherName:$developerName}), (game:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                    "SET d:Developer, d.DeveloperName = $developerName " +
                    "MERGE (d)<-[:MADE_BY]-(game) Return d";
            Result result = neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
            if (!result.hasNext()) {
                queryString = "Match (game:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "MERGE (n:Developer {DeveloperName:$developerName}) " +
                        "MERGE r = (n)<-[:MADE_BY]-(game) Return r";
                Query query = new Query(queryString, parameters);
                neo4JDatabaseHelper.runQuery(query);
            }
            String checkExistsQuery = "MATCH (d:Developer {DeveloperName:$developerName})<-[r:MADE_BY]-(game:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName})" +
                    "Return r";
            Query query = new Query(checkExistsQuery, parameters);
            Result exists = neo4JDatabaseHelper.runQuery(query);
            developerExists = exists.hasNext();
        }
    }

    public ArrayList<String> getDevelopers() {
        String triviaQuery = "MATCH (p:Developer) RETURN p.DeveloperName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        ArrayList<String> developers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            developers.add(String.valueOf(row.get("p.DeveloperName")));
        }
        return developers;
    }

    public ArrayList<String> getDevelopers(HashMap<String, Object> parameters) {
        String triviaQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[:MADE_BY]-(d:Developer) RETURN d.DeveloperName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<String> developers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            developers.add(String.valueOf(row.get("d.DeveloperName")));
        }
        return developers;
    }
}
