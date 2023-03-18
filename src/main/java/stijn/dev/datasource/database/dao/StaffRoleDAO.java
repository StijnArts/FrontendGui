package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class StaffRoleDAO {

    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();

    public ArrayList<String> getStaffRoles() {
        String staffQuery = "MATCH (s:StaffRole) RETURN s.Name";
        Result result = neo4JDatabaseHelper.runQuery(new Query(staffQuery));
        ArrayList<String> staff = new ArrayList<>();
        while(result.hasNext()) {
            Map<String, Object> row = result.next().asMap();
            staff.add(String.valueOf(row.get("s.Name")));
        }
        return staff;
    }
}
