package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.data.*;

import java.util.*;

public class AdditionalAppDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<AdditionalApp> getAdditionalApps(HashMap<String, Object> parameters) {
        String characterQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[a:ADDITIONAL_APP]-(g) RETURN a.Path, a.Name, a.Arguments";
        Result result = neo4JDatabaseHelper.runQuery(new Query(characterQuery,parameters));
        ArrayList<AdditionalApp> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new AdditionalApp(String.valueOf(row.get("a.Path")),String.valueOf(row.get("a.Name")),
                    String.valueOf(row.get("a.Arguments"))));
        }
        return staff;
    }
}
