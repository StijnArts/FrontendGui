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
        createBaseNodes();
        databaseProperties.isInitialized(true);
        System.out.println("Database Has Been Initialized.");
    }

    private void createBaseNodes() {
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCFrontCover', Name: 'Front Cover (NTSC)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPFrontCover', Name: 'Front Cover (JP)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALFrontCover', Name: 'Front Cover (PAL)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCSpine', Name: 'Spine (NTSC)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPSpine', Name: 'Spine (JP)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALSpine', Name: 'Spine (PAL)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCBackCover', Name: 'Back Cover (NTSC)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPBackCover', Name: 'Back Cover (JP)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALBackCover', Name: 'Back Cover (PAL)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCMedia', Name: 'Media (NTSC)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPMedia', Name: 'Media (JP)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALMedia', Name: 'Media (PAL)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCTrailer', Name: 'Trailer (NTSC)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPTrailer', Name: 'Trailer (JP)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALTrailer', Name: 'Trailer (PAL)'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Soundtrack', Name: 'Soundtrack'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'OVA', Name: 'OVA'}) return g");
        //TODO When a series or anime is added a separate node should be created with each of the episodes that are a part
        //of that series connected to it
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Movie', Name: 'Movie'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Screenshot', Name: 'Screenshot'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'ConceptArt', Name: 'Concept Art'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Render', Name: 'Render'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PromotionalMaterial', Name: 'PromotionalMaterial'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Book', Name: 'Book'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Manga', Name: 'Manga'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Comic', Name: 'Comic'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'ArtBook', Name: 'ArtBook'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Manual', Name: 'Manual'}) return g");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Guide', Name: 'Guide'}) return g");
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
