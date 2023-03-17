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

    private synchronized void initialize(){
        createConstraints();
        createBaseNodes();
        databaseProperties.isInitialized(true);
        System.out.println("Database Has Been Initialized.");
    }

    private void createBaseNodes() {
        createGalleryNodes();
        createBasePriorityNodes();
        createRatingNodes();
        createMultiplayerTypeNodes();
    }

    private void createMultiplayerTypeNodes() {
        runQuery("MERGE (g:Playmode {Name: 'Singleplayer'}) return g");
        runQuery("MERGE (g:Playmode {Name: 'Online Multiplayer'}) return g");
        runQuery("MERGE (g:Playmode {Name: 'Local Multiplayer'}) return g");
        runQuery("MERGE (g:Playmode {Name: 'Online Co-op'}) return g");
        runQuery("MERGE (g:Playmode {Name: 'Local Co-op'}) return g");
    }

    private void createRatingNodes() {
        runQuery("MERGE (g:Rating {Rating: 'RP - Rating Pending', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'RP - Rating Pending – Likely Mature 17+', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'E - Everyone', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'E10+ - Everyone 10+', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'T - Teen', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'M - Mature 17+', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'AO - Adults Only', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'EC - Early Childhood', Organization: 'ESRB'}) return g");
        runQuery("MERGE (g:Rating {Rating: '3', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: '7', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: '12', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: '16', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: '18', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Bad Language', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Discrimination', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Drugs', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Fear/Horror', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Gambling', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Sex', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Violence', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'In-Game Purchases', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: '!', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Online', Organization: 'PEGI'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'All Ages', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Ages 12 and up', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Ages 15 and up', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Ages 17 and up', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Ages 18 and up only', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Educational/Database', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'CERO Regulations-Compatible', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Rating Scheduled', Organization: 'CERO'}) return g");
        runQuery("MERGE (g:Rating {Rating: 'Bad Language', Organization: 'CERO'}) return g");
    }

    private void createBasePriorityNodes() {
        runQuery("MERGE (g:Priority {UniqueName: 'Filler', Priority: 3}) return g");
        runQuery("MERGE (g:Priority {UniqueName: 'Classic', Priority: 2}) return g");
        runQuery("MERGE (g:Priority {UniqueName: 'Staple', Priority: 1}) return g");
    }

    private void createGalleryNodes() {
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
