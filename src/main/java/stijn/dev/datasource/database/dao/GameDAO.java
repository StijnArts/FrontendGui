package stijn.dev.datasource.database.dao;

import org.neo4j.driver.*;
import stijn.dev.datasource.database.*;
import stijn.dev.datasource.objects.items.*;

import java.util.*;

public class GameDAO {
    private Neo4JDatabaseHelper neo4JDatabaseHelper = new Neo4JDatabaseHelper();
    private TriviaDAO triviaDAO = new TriviaDAO();
    private PublisherDAO publisherDAO = new PublisherDAO();
    private DeveloperDAO developerDAO = new DeveloperDAO();
    private TagDAO tagDAO = new TagDAO();
    private TerritoryDAO territoryDAO = new TerritoryDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private AlternateNameDAO alternateNameDAO = new AlternateNameDAO();
    private CharacterDAO characterDAO = new CharacterDAO();
    private RelatedGameDAO relatedGameDAO = new RelatedGameDAO();
    private AdditionalAppDAO additionalAppDAO = new AdditionalAppDAO();
    private RatingDAO ratingDAO = new RatingDAO();
    private PriorityDataDAO priorityDataDAO = new PriorityDataDAO();
    private PlaymodeDAO playmodeDAO = new PlaymodeDAO();
    public ArrayList<Game> getGames(){
        String query = "Match (g:Game)-[:ON_PLATFORM]-(p:Platform) " +
                "Return g.GameName, g.GameId, g.Description, g.Theme, g.DefaultSummary, g.DefaultSortingTitle, " +
                "g.LaunchParameters, g.GamePath, g.CommunityRating, g.CommunityRatingCount, g.MaxPlayers, g.HLTBStory, g.HLTBStoryAndExtra, g.HLTBCompletionist," +
                "g.Cooperative, p.PlatformName";
        Result result = neo4JDatabaseHelper.runQuery(query);
        ArrayList<Game> gameItems = new ArrayList<>();
        while(result.hasNext()){
            Map<String, Object> row = result.next().asMap();
            Game game = createGameFromDatabase(row);
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("gameName",game.getName());
            parameters.put("platformName",game.getPlatform());
            gameItems.add(game);
            //Metadata tab
            game.setPlaymodes(playmodeDAO.getPlaymodes(parameters));
            game.setSortingTitle(String.valueOf(row.get("g.DefaultSortingTitle")));
            game.setPriority(priorityDataDAO.getPriority(parameters));
            game.setSummary(String.valueOf(row.get("g.DefaultSummary")));
            game.setTrivia(triviaDAO.getTrivia(parameters));
            game.setPublisher(publisherDAO.getPublishers(parameters));
            game.setDeveloper(developerDAO.getDevelopers(parameters));
            game.setTags(tagDAO.getTags(parameters));
            game.setReleaseDates(territoryDAO.getReleaseDates(parameters));
            game.setStaff(staffDAO.getStaff(parameters));
            game.setRatings(ratingDAO.getRatings(parameters));
            game.setCharacters(characterDAO.getCharacters(parameters));
            game.setAlternateNames(alternateNameDAO.getAlternateNames(parameters));
            game.setRelatedGames(relatedGameDAO.getRelatedGames(parameters));
            game.setAdditionalApps(additionalAppDAO.getAdditionalApps(parameters));
            //Collection Tab
            String seriesQuery;
            String franchiseQuery;

            //TODO write queries to get all relationships for games
            //System.out.println(game);
        }
        return gameItems;
    }

    private Game createGameFromDatabase(Map<String, Object> row){
        return new Game(String.valueOf(row.get("g.GameName")), String.valueOf(row.get("g.GamePath")), String.valueOf(row.get("g.GameId")),
                String.valueOf(row.get("g.Description")), String.valueOf(row.get("g.MaxPlayers")),
                String.valueOf(row.get("p.PlatformName")), String.valueOf(row.get("g.CommunityRating")), String.valueOf(row.get("g.CommunityRatingCount")),
                Boolean.valueOf(String.valueOf(row.get("g.Cooperative"))));
    }
}
