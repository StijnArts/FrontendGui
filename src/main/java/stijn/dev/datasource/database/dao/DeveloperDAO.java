package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class DeveloperDAO {

    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public void createDeveloper(GameImportItem gameImportItem) {
        if(!gameImportItem.getDeveloper().equals("N/A")) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("companyName", gameImportItem.getDeveloper().trim());
                parameters.put("gameId", gameImportItem.getGameId());
                parameters.put("gameName", gameImportItem.getName());
                parameters.put("platformName", gameImportItem.getPlatform());
                String queryString = "MERGE (c:Company {CompanyName:$companyName}) " +
                        "With c " +
                        "MATCH (game:Game {GameName:$gameName, GameId:$gameId})-[:ON_PLATFORM]-(p:Platform {PlatformName:$platformName}) " +
                        "WITH c, game " +
                        "MERGE (c)<-[:MADE_BY]-(game) Return c";
                neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
        }
    }

    public void createDeveloper(HashMap<String, Object> parameters) {
            String queryString = "MERGE (c:Company {CompanyName:$companyName}) " +
                    "With c " +
                    "MATCH (game:Game) " +
                    "WHERE ID(game) = $id " +
                    "MERGE (c)<-[:MADE_BY]-(game)";
            neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));

    }

    public ArrayList<String> getDevelopers() {
        String triviaQuery = "MATCH (p:Company) RETURN p.CompanyName ORDER BY p.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        ArrayList<String> developers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            developers.add(String.valueOf(row.get("p.CompanyName")));
        }
        return new ArrayList<>(developers.stream().filter(developer -> !developer.isBlank()).toList());
    }

    public List<String> getDevelopers(HashMap<String, Object> parameters) {
        String query = "MATCH (d:Company)-[:MADE_BY]-(g:Game) " +
                "WHERE ID(g) = $id " +
                "RETURN d.CompanyName " +
                "ORDER BY p.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        ArrayList<String> developers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            developers.add(String.valueOf(row.get("d.CompanyName")));
        }
        return developers.stream().filter(developer -> !developer.isBlank()).toList();
    }
}
