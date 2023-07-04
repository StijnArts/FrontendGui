package stijn.dev.resource.service;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.database.dao.*;
import stijn.dev.datasource.objects.data.*;
import stijn.dev.datasource.objects.items.*;
import stijn.dev.datasource.objects.items.Character;
import stijn.dev.resource.controllers.*;

import java.util.*;

public class EditService {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private PriorityDataDAO priorityDataDAO = new PriorityDataDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private PlaymodeDAO playmodeDAO = new PlaymodeDAO();
    private AlternateNameDAO alternateNameDAO = new AlternateNameDAO();
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private RelatedGameDAO relatedGameDAO = new RelatedGameDAO();
    private RelationshipDAO relationshipDAO = new RelationshipDAO();
    private GameDAO gameDAO = new GameDAO();
    private PlatformDAO platformDAO = new PlatformDAO();
    private AdditionalAppDAO additionalAppDAO = new AdditionalAppDAO();
    private TriviaDAO triviaDAO = new TriviaDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private TagDAO tagDAO = new TagDAO();
    private StaffRoleDAO staffRoleDAO = new StaffRoleDAO();
    private CharacterDAO characterDAO = new CharacterDAO();
    private CharacterRoleDAO characterRoleDAO = new CharacterRoleDAO();

    public void saveChanges(Game originalGame, EditController editController) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gameName", originalGame.getName());
        parameters.put("platformName", originalGame.getPlatform());
        parameters.put("id", Integer.valueOf(originalGame.getDatabaseId()));
        String coreChangeQuery = "MATCH (g:Game) " +
                "WHERE ID(g) = $id ";
        saveDevelopers(parameters, editController);
        savePublishers(parameters, editController);
        saveRatings(parameters, editController);
        savePlayModes(parameters, editController);

        //Metadata
        saveAlternateName(parameters, editController);
        saveRelatedGames(parameters, editController);
        saveAdditionalApps(parameters, editController);
        saveTrivia(parameters, editController);
        saveStaff(parameters, editController);
        saveCharacters(parameters, editController);
        saveTags(parameters, editController);
        saveReleaseDates(parameters, editController);

        //TODO Media


        //Has to be done last
        saveGeneral(coreChangeQuery, parameters, editController);
        savePlatform(parameters, editController);
    }

    private void saveReleaseDates(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:RELEASED_IN]-(:Territory) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object tag : editController.getReleaseDatesTable().getItems()){
            if(tag instanceof ReleaseDate){
                if(!((ReleaseDate) tag).getTerritory().getValue().isEmpty()){
                    HashMap<String, Object> releaseDateParameters = new HashMap<>();
                    releaseDateParameters.putAll(parameters);
                    releaseDateParameters.put("date",((ReleaseDate) tag).getDate().getValue().toString());
                    releaseDateParameters.put("territory", ((ReleaseDate) tag).getTerritory().getValue());
                    territoryDAO.createReleaseDateEdge(releaseDateParameters);
                }
            }
        }
    }

    private void saveTags(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:HAS_TAG]-(:Tag) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object tag : editController.getTagTable().getItems()){
            if(tag instanceof Tag){
                if(!((Tag) tag).getName().isEmpty()){
                    HashMap<String, Object> tagParameters = new HashMap<>();
                    tagParameters.putAll(parameters);
                    tagParameters.put("tag",((Tag) tag).getName());
                    tagDAO.createTagEdge(tagParameters);
                }
            }
        }
    }

    private void saveCharacters(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:FEATURED_IN]-(:Character) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object character : editController.getCharacterTable().getItems()){
            if(character instanceof Character){
                if(!((Character) character).getCharacterID().isEmpty()){
                    HashMap<String, Object> characterParameters = new HashMap<>();
                    characterParameters.putAll(parameters);
                    characterParameters.put("characterId",((Character) character).getCharacterID());
                    characterParameters.put("characterName",((Character) character).getName());
                    characterParameters.put("role",((Character) character).getRole());
                    if(!((Character) character).getVoiceActor().isBlank()){
                        characterParameters.put("voiceActor",((Character) character).getVoiceActor());
                        characterDAO.createCharacterEdgeWithVoiceActor(characterParameters);
                        return;
                    }

                    characterDAO.createCharacterEdge(characterParameters);
                }
            }
        }
    }

    private void saveStaff(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:WORKED_ON]-(:Staff) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object staff : editController.getStaffTable().getItems()){
            if(staff instanceof Staff){
                if(!((Staff) staff).getStaffID().isEmpty()){
                    HashMap<String, Object> staffParameters = new HashMap<>();
                    staffParameters.putAll(parameters);
                    staffParameters.put("staffId",((Staff) staff).getStaffID());
                    staffParameters.put("firstName",((Staff) staff).getFirstName());
                    staffParameters.put("lastName",((Staff) staff).getLastName());
                    staffParameters.put("role",((Staff) staff).getRole());
                    staffDAO.createStaffEdge(staffParameters);
                }
            }
        }
    }

    private void saveTrivia(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:TRIVIA_ABOUT]-(:Trivia) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object trivia : editController.getTriviaTable().getItems()){
            if(trivia instanceof Trivia){
                if(!((Trivia) trivia).getTriviaID().get().isEmpty()){
                    HashMap<String, Object> triviaParameters = new HashMap<>();
                    triviaParameters.putAll(parameters);
                    triviaParameters.put("triviaId",((Trivia) trivia).getTriviaID().getValue());
                    triviaParameters.put("fact",((Trivia) trivia).getFact());
                    triviaDAO.createTriviaEdge(triviaParameters);
                }
            }
        }
    }

    private void saveAdditionalApps(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:ADDITIONAL_APP]-(g) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object additionalApp : editController.getAdditionalAppsTable().getItems()){
            if(additionalApp instanceof AdditionalApp){
                if(!((AdditionalApp) additionalApp).getPath().get().isEmpty()){
                    HashMap<String, Object> additionalAppParameters = new HashMap<>();
                    additionalAppParameters.putAll(parameters);
                    additionalAppParameters.put("path",((AdditionalApp) additionalApp).getPath().getValue());
                    additionalAppParameters.put("additionalAppName",((AdditionalApp) additionalApp).getName().getValue());
                    additionalAppParameters.put("arguments", ((AdditionalApp) additionalApp).getArguments().getValue());
                    additionalAppDAO.createAdditionalApp(additionalAppParameters);
                }
            }
        }
    }

    private void saveRelatedGames(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:RELATED_TO]-(:Game) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object relatedGame : editController.getRelatedGameTable().getItems()){
            if(relatedGame instanceof RelatedGame){
                if(!((RelatedGame) relatedGame).getId().get().isEmpty()){
                    HashMap<String, Object> relatedGameParameters = new HashMap<>();
                    relatedGameParameters.putAll(parameters);
                    relatedGameParameters.put("relationshipType",((RelatedGame) relatedGame).getRelationship().getValue());
                    relatedGameParameters.put("relationshipDescription",((RelatedGame) relatedGame).getDescription().getValue());
                    relatedGameParameters.put("relatedGameId", Integer.parseInt(((RelatedGame) relatedGame).getId().getValue()));
                    relatedGameDAO.createRelatedGameEdge(relatedGameParameters);
                }
            }
        }
    }

    private void saveAlternateName(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:ALTERNATE_NAME]-(a) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for(Object alternateName : editController.getAlternateNamesTable().getItems()){
            if(alternateName instanceof AlternateName){
                if(!((AlternateName) alternateName).getAlternateNameID().isEmpty().get()){
                    HashMap<String, Object> alternateNameParameters = new HashMap<>();
                    alternateNameParameters.putAll(parameters);
                    alternateNameParameters.put("alternateName",((AlternateName) alternateName).getAlternateName().getValue());
                    alternateNameParameters.put("alternateNameId",((AlternateName) alternateName).getAlternateNameID().getValue());
                    if(!((AlternateName) alternateName).getReason().isEmpty().get() || !((AlternateName) alternateName).getReason().get().equals("Community")){
                        alternateNameParameters.put("region",((AlternateName) alternateName).getReason().getValue());
                        alternateNameDAO.createAlternateNameWithRegion(alternateNameParameters);
                    } else {
                        alternateNameDAO.createGameAlternateName(alternateNameParameters);
                    }
                }
            }
        }
    }

    private void savePlatform(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game)" +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:ON_PLATFORM]-(:Platform) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        String platformName = editController.getPlatformComboBox().getValue();
        if(platformName.isEmpty()){
            platformName = editController.getGame().getPlatform();
        }
        HashMap<String, Object> platformParameters = new HashMap<>();
        platformParameters.putAll(parameters);
        platformParameters.put("name",platformName.trim());
        platformDAO.createEdgePlatform(platformParameters, platformName.trim());
    }

    private void savePlayModes(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:HAS_MODE]-(:PlayMode) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for (String playMode : editController.getPlayModesComboCheckbox().getValue().itemProperty().getValue().split("; ")) {
            HashMap<String, Object> playModeParameters = new HashMap<>();
            playModeParameters.putAll(parameters);
            playModeParameters.put("name",playMode);
            playmodeDAO.createEdgePlayMode(playModeParameters);
        }
    }

    private void saveRatings(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:HAS_RATING]-(:Rating) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        String[] ratings = editController.getRatingComboCheckBox().getValue().itemProperty().getValue().split("; ");
        for (String rating : ratings) {
            if(!rating.isBlank()){
                HashMap<String, Object> ratingParameters = new HashMap<>(parameters);
                ratingParameters.put("organization",rating.split(": ")[0]);
                ratingParameters.put("rating",rating.split(": ")[1]);
                ratingDAO.createEdgeRating(ratingParameters);
            }
        }
    }

    private void saveDevelopers(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:MADE_BY]-(:Company) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for (String developer : editController.getDeveloperComboCheckBox().getValue().itemProperty().getValue().split("; ")) {
            if(!developer.isBlank()&&!developer.trim().equals(";")){
                HashMap<String, Object> developerParameters = new HashMap<>();
                developerParameters.putAll(parameters);
                developerParameters.put("companyName",developer.trim());
                developerDAO.createDeveloper(developerParameters);
            }
        }
    }

    private void savePublishers(HashMap<String, Object> parameters, EditController editController) {
        String query = "MATCH (g:Game) " +
                "WHERE ID(g) = $id " +
                "WITH g " +
                "MATCH (g)-[r:PUBLISHED_BY]-(:Company) DELETE r";
        neo4JDatabaseHelper.runQuery(new Query(query,parameters));
        for (String publisher : editController.getPublisherComboCheckBox().getValue().itemProperty().getValue().split("; ")) {
            if(!publisher.isBlank()&&!publisher.trim().equals(";")){
                HashMap<String, Object> publisherParameters = new HashMap<>();
                publisherParameters.putAll(parameters);
                publisherParameters.put("companyName",publisher.trim());
                publisherDAO.createPublisher(publisherParameters);
            }
        }
    }

    private void saveGeneral(String coreChangeQuery, HashMap<String, Object> parameters, EditController editController) {
        parameters.put("newGameName", editController.getTitleField().getText());
        parameters.put("newMaxPlayers", editController.getMaxPlayersField().getText());
        parameters.put("newSortingTitle", editController.getDefaultSortingTitleTextField().getText());
        parameters.put("newDefaultSummary", editController.getSummaryTextArea().getText());
        parameters.put("newDescription", editController.getDescriptionTextArea().getText());
        parameters.put("unitsSold", editController.getUnitsSoldField().getText());
        String updateQuery = coreChangeQuery;
        updateQuery = updateQuery + "Set ";
        updateQuery = updateQuery +"g.GameName=$newGameName, ";
        if(editController.getPriorityComboBox().getValue().getName()!=null){
            updateQuery = updateQuery +"g.Priority=$newPriority, ";
            parameters.put("newPriority", editController.getPriorityComboBox().getValue().getName());
        }
        updateQuery = updateQuery +"g.MaxPlayers=$newMaxPlayers, ";
        updateQuery = updateQuery +"g.UnitsSold=$unitsSold, ";
        updateQuery = updateQuery +"g.DefaultSortingTitle=$newSortingTitle, ";
        updateQuery = updateQuery +"g.DefaultSummary=$newDefaultSummary, ";
        updateQuery = updateQuery +"g.Description=$newDescription";
        neo4JDatabaseHelper.runQuery(new Query(updateQuery,parameters));
        //System.out.println(updateQuery);
    }
}
