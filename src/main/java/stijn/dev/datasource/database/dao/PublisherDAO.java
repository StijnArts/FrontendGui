package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class PublisherDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createPublisher(String platformName, List<String> publishers) {
        for (String publisher : publishers) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("companyName", publisher.trim());
                parameters.put("platformName", platformName);
                String queryString = "MATCH (platform:Platform {PlatformName:$platformName}) " +
                        "WITH platform " +
                        "MERGE (d:Company {CompanyName:$companyName}) " +
                        "WITH platform, d " +
                        "MERGE (d)<-[:MADE_BY]-(platform) Return d";
                neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
        }

    }

    public void createPublisher(GameImportItem gameImportItem) {
        if(!gameImportItem.getPublisher().equals("N/A")) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("companyName", gameImportItem.getPublisher().trim());
                parameters.put("gameId", gameImportItem.getGameId());
                parameters.put("gameName", gameImportItem.getName());
                parameters.put("platformName", gameImportItem.getPlatform());
                String queryString = "MATCH (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "WITH game " +
                        "MERGE (d:Company {CompanyName:$companyName}) " +
                        "MERGE (d)<-[:PUBLISHED_BY]-(game) Return d";
                neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
        }
    }

    public void createPublisher(HashMap<String, Object> parameters) {
        String queryString = "MATCH (game:Game {GameName:$gameName})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                "WHERE ID(game) = $id " +
                "WITH game " +
                "MERGE (d:Company {CompanyName:$companyName}) " +
                "MERGE (d)<-[:PUBLISHED_BY]-(game) Return d";
        neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
    }

    public ArrayList<String> getPublishers(){
        String triviaQuery = "MATCH (p:Company) RETURN p.CompanyName " +
                "ORDER BY p.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        ArrayList<String> publishers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            publishers.add(String.valueOf(row.get("p.CompanyName")));
        }
        return new ArrayList<>(publishers.stream().filter(developer -> !developer.isBlank()).toList());
    }

    public ArrayList<String> getPublishers(HashMap<String, Object> parameters){
        String triviaQuery = "MATCH (p:Company)-[:PUBLISHED_BY]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN p.CompanyName " +
                "ORDER BY p.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<String> publishers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            publishers.add(String.valueOf(row.get("p.CompanyName")));
        }
        return new ArrayList<>(publishers.stream().filter(developer -> !developer.isBlank()).toList());
    }

    public ArrayList<String> getPlatformPublishers(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String triviaQuery = "MATCH (p:Company)-[:MADE_BY]-(platform:Platform) " +
                "WHERE ID(platform) = $id " +
                "RETURN p.CompanyName " +
                "ORDER BY p.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<String> publishers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            publishers.add(String.valueOf(row.get("p.CompanyName")));
        }
        return new ArrayList<>(publishers.stream().filter(developer -> !developer.isBlank()).toList());
    }
}
