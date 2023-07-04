package stijn.dev.datasource.database;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.importing.xml.*;
import stijn.dev.datasource.objects.items.*;
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
    }

    public synchronized void initialize(){
        if(!databaseProperties.isInitialized()) {
            createConstraints();
            createBaseNodes();
            System.out.println("Database Has Been Initialized.");
            PlatformDAO platformDAO = new PlatformDAO();
            PlatformXMLParser platformXMLParser = new PlatformXMLParser();
            PublisherDAO publisherDAO = new PublisherDAO();
            ManufacturerDAO manufacturerDAO = new ManufacturerDAO();
            for (String platformName :
                    PlatformXMLParser.getPlatforms()) {
                Platform platform = platformXMLParser.parsePlatform(platformName);
                platformDAO.savePlatform(platform);
                if(platform.getPublishers()!=null){
                    publisherDAO.createPublisher(platform.getPlatformName(), platform.getPublishers());
                }
                if(platform.getManufacturers()!=null){
                    manufacturerDAO.createManufacturer(platform.getPlatformName(), platform.getManufacturers());
                }
            }
            databaseProperties.isInitialized(true);
        }
    }

    private void createBaseNodes() {
        createGalleryNodes();
        createBasePriorityNodes();
        createRatingNodes();
        createMultiplayerTypeNodes();
        createRelationshipNodes();
        createStaffRoleNodes();
        createCharacterRolesNodes();
        createSpecificationTypes();
        createPlatformGalleryNodes();
    }

    private void createCharacterRolesNodes() {
        runQuery("MERGE (s:CharacterRole {Name: 'Protagonist'}) return s");
        runQuery("MERGE (s:CharacterRole {Name: 'Antagonist'}) return s");
        runQuery("MERGE (s:CharacterRole {Name: 'Love Interest'}) return s");
        runQuery("MERGE (s:CharacterRole {Name: 'Confidant'}) return s");
        runQuery("MERGE (s:CharacterRole {Name: 'Supporting'}) return s");
        runQuery("MERGE (s:CharacterRole {Name: 'Foil'}) return s");
    }

    private void createStaffRoleNodes() {
        runQuery("MERGE (s:StaffRole {Name: 'Game Designer'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Systems Designer'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Level Designer'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Game Programmer'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'AI Programmer'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Gameplay Engineer'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Game Artist'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Character Artist'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Environment Artist'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Asset Artist'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Art Director'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Game Director'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'FX Artist'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Concept Artist'}) return s");
        runQuery("MERGE (s:StaffRole {Name: 'Voice Actor'}) return s");
    }

    private void createRelationshipNodes() {
        runQuery("MERGE (r:GameRelationship {Name: 'Port'}) return r");
        runQuery("MERGE (r:GameRelationship {Name: 'Spiritual Successor'}) return r");
        runQuery("MERGE (r:GameRelationship {Name: 'Remaster'}) return r");
        runQuery("MERGE (r:GameRelationship {Name: 'Sequel'}) return r");
        runQuery("MERGE (r:GameRelationship {Name: 'Prequel'}) return r");
        runQuery("MERGE (r:GameRelationship {Name: 'Counterpart'}) return r");
        runQuery("MERGE (r:GameRelationship {Name: 'Remake'}) return r");

        runQuery("MERGE (r:PlatformRelationship {Name: 'Rival'}) return r");
        runQuery("MERGE (r:PlatformRelationship {Name: 'Spiritual Successor'}) return r");
        runQuery("MERGE (r:PlatformRelationship {Name: 'Successor'}) return r");
        runQuery("MERGE (r:PlatformRelationship {Name: 'Predecessor'}) return r");
        runQuery("MERGE (r:PlatformRelationship {Name: 'Counterpart'}) return r");
        runQuery("MERGE (r:PlatformRelationship {Name: 'Can Emulate'}) return r");

    }

    private void createMultiplayerTypeNodes() {
        runQuery("MERGE (g:PlayMode {Name: 'Singleplayer'})");
        runQuery("MERGE (g:PlayMode {Name: 'Online Multiplayer'})");
        runQuery("MERGE (g:PlayMode {Name: 'Local Multiplayer'})");
        runQuery("MERGE (g:PlayMode {Name: 'Online Co-op'})");
        runQuery("MERGE (g:PlayMode {Name: 'Local Co-op'})");
    }

    private void createRatingNodes() {
        runQuery("MERGE (g:Rating {Rating: 'RP - Rating Pending', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'RP - Rating Pending – Likely Mature 17+', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'E - Everyone', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'E10+ - Everyone 10+', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'T - Teen', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'M - Mature 17+', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'AO - Adults Only', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: 'EC - Early Childhood', Organization: 'ESRB'})");
        runQuery("MERGE (g:Rating {Rating: '3', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: '7', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: '12', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: '16', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: '18', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Bad Language', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Discrimination', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Drugs', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Fear/Horror', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Gambling', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Sex', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Violence', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'In-Game Purchases', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Exclamation', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'Online', Organization: 'PEGI'})");
        runQuery("MERGE (g:Rating {Rating: 'All Ages', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Ages 12 and up', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Ages 15 and up', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Ages 17 and up', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Ages 18 and up only', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Educational/Database', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'CERO Regulations-Compatible', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Rating Scheduled', Organization: 'CERO'})");
        runQuery("MERGE (g:Rating {Rating: 'Bad Language', Organization: 'CERO'})");
    }

    private void createBasePriorityNodes() {
        runQuery("MERGE (g:Priority {UniqueName: 'Filler', Priority: 3})");
        runQuery("MERGE (g:Priority {UniqueName: 'Classic', Priority: 2})");
        runQuery("MERGE (g:Priority {UniqueName: 'Staple', Priority: 1})");
    }

    private void createSpecificationTypes(){
        runQuery("MERGE (g:SpecificationType {Name: 'Display'})");
        runQuery("MERGE (g:SpecificationType {Name: 'Cpu'})");
        runQuery("MERGE (g:SpecificationType {Name: 'Graphics'})");
        runQuery("MERGE (g:SpecificationType {Name: 'Memory'})");
        runQuery("MERGE (g:SpecificationType {Name: 'Sound'})");
    }

    private void createGalleryNodes() {
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCFrontCover', Name: 'Front Cover (NTSC)'}) " +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPFrontCover', Name: 'Front Cover (JP)'}) " +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALFrontCover', Name: 'Front Cover (PAL)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCSpine', Name: 'Spine (NTSC)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPSpine', Name: 'Spine (JP)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALSpine', Name: 'Spine (PAL)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCBackCover', Name: 'Back Cover (NTSC)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPBackCover', Name: 'Back Cover (JP)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALBackCover', Name: 'Back Cover (PAL)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCMedia', Name: 'Media (NTSC)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPMedia', Name: 'Media (JP)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALMedia', Name: 'Media (PAL)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCTrailer', Name: 'Trailer (NTSC)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPTrailer', Name: 'Trailer (JP)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALTrailer', Name: 'Trailer (PAL)'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Soundtrack', Name: 'Soundtrack'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'OVA', Name: 'OVA'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        //TODO When a series or anime is added a separate node should be created with each of the episodes that are a part
        //of that series connected to it
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Movie', Name: 'Movie'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Screenshot', Name: 'Screenshot'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'ConceptArt', Name: 'Concept Art'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Render', Name: 'Render'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PromotionalMaterial', Name: 'PromotionalMaterial'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Book', Name: 'Book'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Manga', Name: 'Manga'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Comic', Name: 'Comic'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'ArtBook', Name: 'ArtBook'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Manual', Name: 'Manual'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) " +
                "MERGE (g)-[:USED_BY_PLATFORMS]-(g) ");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Guide', Name: 'Guide'})" +
                "MERGE (g)-[:USED_BY_GAMES]-(g) ");
    }

    private void createPlatformGalleryNodes() {
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCFrontCover', Name: 'Front Cover (NTSC)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPFrontCover', Name: 'Front Cover (JP)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALFrontCover', Name: 'Front Cover (PAL)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCSpine', Name: 'Spine (NTSC)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPSpine', Name: 'Spine (JP)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALSpine', Name: 'Spine (PAL)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCBackCover', Name: 'Back Cover (NTSC)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPBackCover', Name: 'Back Cover (JP)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALBackCover', Name: 'Back Cover (PAL)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCMedia', Name: 'Media (NTSC)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPMedia', Name: 'Media (JP)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALMedia', Name: 'Media (PAL)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'NTSCTrailer', Name: 'Trailer (NTSC)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'JPTrailer', Name: 'Trailer (JP)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PALTrailer', Name: 'Trailer (PAL)'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Soundtrack', Name: 'Soundtrack'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'OVA', Name: 'OVA'})");
        //TODO When a series or anime is added a separate node should be created with each of the episodes that are a part
        //of that series connected to it
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Movie', Name: 'Movie'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Screenshot', Name: 'Screenshot'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'ConceptArt', Name: 'Concept Art'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Render', Name: 'Render'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'PromotionalMaterial', Name: 'PromotionalMaterial'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Book', Name: 'Book'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Manga', Name: 'Manga'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Comic', Name: 'Comic'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'ArtBook', Name: 'ArtBook'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Manual', Name: 'Manual'})");
        runQuery("MERGE (g:GalleryCategory {UniqueName: 'Guide', Name: 'Guide'})");
    }

    private void createConstraints(){
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Company)         REQUIRE n.CompanyName   IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Territory)       REQUIRE n.TerritoryName   IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:GalleryCategory) REQUIRE n.CategoryName    IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Franchise)       REQUIRE n.UniqueName      IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Series)          REQUIRE n.UniqueName      IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Canon)           REQUIRE n.UniqueName      IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Playlist)        REQUIRE n.UniqueName      IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Timeline)        REQUIRE n.UniqueName      IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Staff)           REQUIRE n.StaffID         IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Character)       REQUIRE n.CharacterID     IS UNIQUE");
        runQuery("CREATE CONSTRAINT IF NOT EXISTS FOR (n:Trivia)          REQUIRE n.TriviaID        IS UNIQUE");
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
