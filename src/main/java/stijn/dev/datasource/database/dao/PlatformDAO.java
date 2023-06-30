package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class PlatformDAO {
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private PlatformXMLParser platformXMLParser = new PlatformXMLParser();
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private TerritoryDAO releaseDateDAO = new TerritoryDAO();
    private EmulatorDAO emulatorDAO = new EmulatorDAO();
    public void savePlatform(Platform platformObject) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformName", platformObject.getPlatformName());
        String queryString = "CREATE r = (p:Platform {PlatformName:$platformName";
        if(platformObject.getDescription()!=null){
            parameters.put("description", platformObject.getDescription());
            queryString+=", Description:$description";
        }
        if(platformObject.getSortingTitle()!=null){
            parameters.put("sortingTitle", platformObject.getSortingTitle());
            queryString+=", SortingTitle:$sortingTitle";
        }
        if(platformObject.getMaxPlayers()!=null){
            parameters.put("maxPlayers", platformObject.getMaxPlayers());
            queryString+=", MaxPlayers:$maxPlayers";
        }
        if(platformObject.getCategory()!=null){
            parameters.put("category", platformObject.getCategory());
            queryString+=", Category:$category";
        }
        queryString += "}) ";
        for (String specType : platformObject.getSpecs().keySet()) {
            queryString +=", (p)-[:Specification {SpecificationType:$specType"+specType+",Specification:$spec"+specType+"}]->(p)";
            parameters.put("spec"+specType, platformObject.getSpecs().get(specType));
            parameters.put("specType"+specType, specType);
        }
        queryString += " Return r";
        Query query = new Query(queryString, parameters);
        neo4JDatabaseHelper.runQuery(query);
        territoryDAO.createReleaseDates(platformObject);
    }

    public boolean platformExists(String platform) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformName", platform);
        String query = "Match (platform:Platform {PlatformName:$platformName}) RETURN platform.PlatformName";
        Result systemExistsCheckResult = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        return systemExistsCheckResult.hasNext();
    }

    public void createEdgePlatform(HashMap<String, Object> parameters, String platformName) {
        if(!platformExists(platformName)){
            Platform platform = platformXMLParser.parsePlatform(platformName);
            savePlatform(platform);
            if(platform.getPublisher()!=null){
                publisherDAO.createPublisher(platform);
            }
        }
        String query = "MATCH (g:Game {GameName: $gameName}) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (e:Platform {PlatformName: $name}) " +
                "MERGE (g)-[:ON_PLATFORM]->(e)";
        /*System.out.println("MATCH (g:Game {GameName: \""+parameters.get("gameName")+"\"})-[:ON_PLATFORM]-(p:Platform {PlatformName: \""+parameters.get("platformName")+"\"}) \n" +
                "                WITH g \n" +
                "                MATCH (e:Rating {Rating: \""+parameters.get("rating")+"\", Organization: \""+parameters.get("organization")+"\"}) \n" +
                "                MERGE (g)-[:HAS_RATING]->(e)");*/
        neo4JDatabaseHelper.runQuery(new Query(query, parameters));
    }

    public String getPlatform(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String platformQuery = "MATCH (p:Platform)-[:ON_PLATFORM]-(g:Game) " +
                "WHERE ID(g) = $id " +
                " RETURN p.PlatformName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(platformQuery,parameters));
        ArrayList<String> platforms = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            platforms.add(row.get("p.PlatformName")+"");
        }
        if(platforms.isEmpty()){
            return "";
        }
        return platforms.get(0);
    }

    public List<Platform> getPlatforms() {
        String query = "MATCH (p:Platform) " +
                "WITH p " +
                "MATCH (p)-[:MADE_BY]-(m:Publisher) " +
                "RETURN ID(p), p.PlatformName, p.SortingTitle, p.Category, p.Description, p.MaxPlayers, p.DefaultEmulator, m.PublisherName";
        Result result = neo4JDatabaseHelper.runQuery(query);
        List<Platform> platforms = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            platforms.add(new Platform(
                    Integer.parseInt(row.get("ID(p)")+""),
                    row.get("p.PlatformName")+"",
                    row.get("p.SortingTitle")+"",
                    releaseDateDAO.getPlatformReleaseDates(Integer.parseInt(row.get("ID(p)")+"")),
                    row.get("m.PublisherName")+"",
                    row.get("p.Description")+"",
                    getSpecs(Integer.parseInt(row.get("ID(p)")+"")),
                    row.get("p.MaxPlayers")+"",
                    row.get("p.Category")+"",
                    emulatorDAO.getDefaultEmulatorForPlatform(Integer.parseInt(row.get("ID(p)")+"")),
                    emulatorDAO.getPlatformEmulators(Integer.parseInt(row.get("ID(p)")+""))
            ));
        }
        return platforms;
    }

    private HashMap<String, String> getSpecs(int platformId) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id",platformId);
        String query = "MATCH (p:Platform) " +
                "WHERE ID(p) = $id " +
                "WITH p " +
                "MATCH (p)-[s:Specification]-(p)" +
                "RETURN s.Specification, s.SpecificationType";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query, parameters));
        HashMap<String, String> specs = new HashMap<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            specs.put(row.get("s.SpecificationType")+"",row.get("s.Specification")+"");
        }
        return specs;
    }

    public void savePlatform(HashMap<String, Object> platformParameters) {
        String query = "MATCH (p:Platform) " +
                        "WHERE ID(p) = $id " +
                        "SET p.Description = $description, p.SortingTitle = $sortingTitle, p.PlatformName = $platformName ";
        if(!platformParameters.get("defaultEmulator").equals("")) {
            query += "WITH p " +
                    "MATCH (e:Emulator {EmulatorName: $defaultEmulator}) " +
                    "Merge (p)-[d:DefaultEmulator]-(e)";
        }
        if(!platformParameters.get("publisher").equals("")) {
            query += "WITH p " +
                    "MATCH (publisher:Publisher {PublisherName: $publisher}) " +
                    "Merge (p)-[d:MADE_BY]-(publisher)";
        }
        query += "RETURN ID(p)";
        Result result = neo4JDatabaseHelper.runQuery(new Query(query,platformParameters));
        if(result.next().asMap().get("ID(p)").equals("")){

            query = "CREATE (p:Platform {Description : $description, SortingTitle : $sortingTitle, PlatformName : $platformName})";
            if(!platformParameters.get("defaultEmulator").equals("")) {
                query += "WITH p " +
                        "MATCH (e:Emulator {EmulatorName: $defaultEmulator}) " +
                        "Merge (p)-[d:DefaultEmulator]-(e)";
            }
            if(!platformParameters.get("publisher").equals("")) {
                query += "WITH p " +
                        "MATCH (publisher:Publisher {PublisherName: $publisher}) " +
                        "Merge (p)-[d:MADE_BY]-(publisher)";
            }
            neo4JDatabaseHelper.runQuery(new Query(query, platformParameters));
        }
    }

    public void deletePlatform(int id) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("id", id);
            String tagQuery = "MATCH (p:Platform) " +
                    "WHERE ID(p) = $id " +
                    "DETACH Delete p";
            neo4JDatabaseHelper.runQuery(new Query(tagQuery,parameters));
    }
}
