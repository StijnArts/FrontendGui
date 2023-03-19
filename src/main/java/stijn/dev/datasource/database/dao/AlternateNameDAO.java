package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class AlternateNameDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public ArrayList<AlternateName> getAlternateNames(HashMap<String, Object> parameters) {
        String communityAlternateNamesQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[a:ALTERNATE_NAME]-(g) RETURN a.AlternateNameID, a.Name";
        Result communityAlternateNamesResult = neo4JDatabaseHelper.runQuery(new Query(communityAlternateNamesQuery,parameters));
        ArrayList<AlternateName> alternateNames = new ArrayList<>();
        while(communityAlternateNamesResult.hasNext()) {
            Map<String, Object> row = communityAlternateNamesResult.next().asMap();
            alternateNames.add(new AlternateName(String.valueOf(row.get("a.AlternateNameID")),String.valueOf(row.get("a.Name")),
                    "Community"));
        }
        String regionAlternateNamesQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[a:ALTERNATE_NAME]-(t:Territory) RETURN a.AlternateNameID, a.Name, t.Name";
        Result regionAlternateNamesResult = neo4JDatabaseHelper.runQuery(new Query(regionAlternateNamesQuery,parameters));
        while(regionAlternateNamesResult.hasNext()) {
            Map<String, Object> row = regionAlternateNamesResult.next().asMap();
            alternateNames.add(new AlternateName(String.valueOf(row.get("a.AlternateNameID")),String.valueOf(row.get("a.Name")),
                    String.valueOf(row.get("t.Name"))));
        }
        return alternateNames;
    }
}
