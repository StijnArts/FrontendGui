package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class StaffDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    public ArrayList<Staff> getStaff(HashMap<String, Object> parameters) {
        String staffQuery = "MATCH (g:Game{GameName:$gameName})-[:ON_PLATFORM]-(p:Platform{PlatformName:$platformName}), " +
                "(g)-[:WORKED_ON]-(s:Staff) RETURN s.StaffID, s.FirstName, s.LastName";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery,parameters));
        ArrayList<Staff> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(new Staff(String.valueOf(row.get("s.StaffID")),String.valueOf(row.get("s.FirstName")),
                    String.valueOf(row.get("s.LastName"))));
        }
        return staff;
    }
}
