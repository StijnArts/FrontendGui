package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;

import java.util.*;

public class ManufacturerDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public List<String> getPlatformManufacturers(int id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        String triviaQuery = "MATCH (m:Company)-[:PRODUCED_BY]-(platform:Platform) " +
                "WHERE ID(platform) = $id " +
                "RETURN m.CompanyName " +
                "ORDER BY m.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery,parameters));
        ArrayList<String> manufacturers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            manufacturers.add(String.valueOf(row.get("m.CompanyName")));
        }
        return new ArrayList<>(manufacturers.stream().filter(developer -> !developer.isBlank()).toList());
    }

    public List<String> getManufacturers() {
        String triviaQuery = "MATCH (m:Company) " +
                "RETURN m.CompanyName " +
                "ORDER BY m.CompanyName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(triviaQuery));
        ArrayList<String> manufacturers = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            manufacturers.add(String.valueOf(row.get("m.CompanyName")));
        }
        return new ArrayList<>(manufacturers.stream().filter(developer -> !developer.isBlank()).toList());
    }

    public void createManufacturer(String platformName, List<String> manufacturers) {
        for (String manufacturer : manufacturers) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("companyName", manufacturer.trim());
            parameters.put("platformName", platformName);
            String queryString = "MATCH (platform:Platform {PlatformName:$platformName}) " +
                    "WITH platform " +
                    "MERGE (d:Company {CompanyName:$companyName}) " +
                    "WITH platform, d " +
                    "MERGE (d)<-[:PRODUCED_BY]-(platform) Return d";
            neo4JDatabaseHelper.runQuery(new Query(queryString, parameters));
        }
    }
}
