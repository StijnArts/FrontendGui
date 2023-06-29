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
    public void createPlatform(Platform platformObject) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformName", platformObject.getPlatformName());
        String queryString = "CREATE r = (p:Platform {PlatformName:$platformName";
        if(platformObject.getDescription()!=null){
            parameters.put("description", platformObject.getDescription());
            queryString+=", Description:$description";
        }
        if(platformObject.getMaxPlayers()!=null){
            parameters.put("maxPlayers", platformObject.getDescription());
            queryString+=", MaxPlayers:$maxPlayers";
        }
        if(platformObject.getCategory()!=null){
            parameters.put("category", platformObject.getDescription());
            queryString+=", Category:$category";
        }
        queryString += "}) ";
        for (String spec : platformObject.getSpecs().keySet()) {
            queryString +=", (p)-[:"+spec+" {spec:$"+spec+"}]->(p)";
            parameters.put(spec, platformObject.getSpecs().get(spec));
        }
        queryString += " Return r";
        Query query = new Query(queryString, parameters);
        neo4JDatabaseHelper.runQuery(query);
        territoryDAO.createReleaseDates(platformObject);
    }

    public boolean platformExists(String platform) {
        String query = "Match (platform:Platform {PlatformName:'" + platform + "'}) RETURN platform.PlatformName";
        Result systemExistsCheckResult = neo4JDatabaseHelper.runQuery(query);
        return systemExistsCheckResult.hasNext();
    }

    public void createEdgePlatform(HashMap<String, Object> parameters, String platformName) {
        if(!platformExists(platformName)){
            Platform platform = platformXMLParser.parsePlatform(platformName);
            createPlatform(platform);
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
        ArrayList<String> ratings = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            ratings.add(row.get("p.PlatformName")+"");
        }
        if(ratings.isEmpty()){
            return "";
        }
        return ratings.get(0);
    }
}
