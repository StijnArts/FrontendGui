package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class PlatformDAO {
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
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
        queryString += "})";
        for (String spec : platformObject.getSpecs().keySet()) {
            queryString +=", (p)-[:"+spec+" {spec:$"+spec+"}]->(p)";
            parameters.put(spec, platformObject.getSpecs().get(spec));
        }
        queryString += " Return r";
        Query query = new Query(queryString, parameters);
        neo4JDatabaseHelper.runQuery(query);
        territoryDAO.createReleaseDates(platformObject);
    }

    public boolean checkIfPlatformExists(String platform) {
        String query = "Match (platform:Platform {PlatformName:'" + platform + "'}) RETURN platform.PlatformName";
        Result systemExistsCheckResult = neo4JDatabaseHelper.runQuery(query);
        return systemExistsCheckResult.hasNext();
    }
}
