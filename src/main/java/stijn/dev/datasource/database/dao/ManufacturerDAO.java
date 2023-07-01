package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;

import java.util.*;

public class ManufacturerDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public List<String> getPlatformManufacturers(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String triviaQuery = "MATCH (m:Manufacturer)-[:PRODUCED_BY]-(platform:Platform) " +
                "WHERE ID(platform) = $id " +
                "RETURN m.ManufacturerName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<String> manufacturers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            manufacturers.add(String.valueOf(row.get("m.ManufacturerName")));
        }
        return manufacturers;
    }

    public List<String> getManufacturers() {
        String triviaQuery = "MATCH (m:Manufacturer) " +
                "RETURN m.ManufacturerName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        ArrayList<String> manufacturers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            manufacturers.add(String.valueOf(row.get("m.ManufacturerName")));
        }
        return manufacturers;
    }

    public void createManufacturer(String platformName, List<String> manufacturers) {
        //TODO merge publisher, manufacturer and developer into one label
        for (String manufacturer : manufacturers) {
            boolean manufacturerExists = false;
            while(!manufacturerExists) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("manufacturerName", manufacturer.trim());
                parameters.put("platformName", platformName);
                String queryString = "MATCH (d:Developer {DeveloperName:$manufacturerName}) " +
                        "SET d:Publisher, d.ManufacturerName = $manufacturerName " +
                        "MERGE (d)<-[:MADE_BY]-(platform) Return d";
                Result result = neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
                if (!result.hasNext()) {
                    queryString = "Match (platform:Platform {PlatformName:$platformName}) " +
                            "MERGE (n:Publisher {ManufacturerName:$manufacturerName})" +
                            "MERGE r = (n)<-[:MADE_BY]-(platform) Return r";
                    Query query = new Query(queryString, parameters);
                    neo4JDatabaseHelper.runQuery(query);
                }
                String checkExistsQuery = "MATCH (pu:Publisher {ManufacturerName:$manufacturerName})<-[r:MADE_BY]-(platform:Platform {PlatformName:$platformName})" +
                        "Return r";
                Query checkExists = new Query(checkExistsQuery, parameters);
                Result exists = neo4JDatabaseHelper.runQuery(checkExists);
                manufacturerExists = exists.hasNext();

            }
        }
    }
}
