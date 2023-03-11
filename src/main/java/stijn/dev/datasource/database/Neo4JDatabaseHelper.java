package stijn.dev.datasource.database;

import org.neo4j.driver.*;
import stijn.dev.settings.*;

public class Neo4JDatabaseHelper implements AutoCloseable{
    private final String user = "neo4j";
    private final String password = "password";
    private final String uri = "bolt://localhost:7687";
    //TODO add indexes on nodes after import and edit
    private DatabaseProperties databaseProperties = new DatabaseProperties();
    private Driver driver;
    public Neo4JDatabaseHelper(){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        if(!databaseProperties.isInitialized()){
            initialize();
        }
    }

    private void initialize(){
        createConstraints();
        databaseProperties.isInitialized(true);
        System.out.println("Database Has Been Initialized.");
    }

    private void createConstraints(){
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Developer)       REQUIRE n.DeveloperName IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Publisher)       REQUIRE n.PublisherName IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Territory)       REQUIRE n.TerritoryName IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:GalleryCategory) REQUIRE n.CategoryName  IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Franchise)       REQUIRE n.UniqueName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Series)          REQUIRE n.UniqueName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Canon)           REQUIRE n.UniqueName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Playlist)        REQUIRE n.UniqueName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Timeline)        REQUIRE n.UniqueName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Character)       REQUIRE n.UniqueName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Trivia)          REQUIRE n.UniqueName    IS UNIQUE");
    }


    @Override
    public void close() throws RuntimeException {
        driver.close();
    }

    public Result runQuery(String query){
        Session session = driver.session();
          return session.run(new Query(query));
    }

    public Result runQuery(Query query){
        Session session = driver.session();
        return session.run(query);
    }

    public Result wipeDatabase(){
        return runQuery("Match (n) Detach Delete (n)");
    }
}
