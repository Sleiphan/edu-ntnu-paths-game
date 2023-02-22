import java.util.List;

/**
 * This class is a digital representation of a "paths"-game, connecting a player to a story with specific goals.
 * It comes with methods to both start and progress the game.
 * @author Håkon F. Fjellanger
 */
public class Game {
    private Player player;
    private Story story;
    private List<Goal> goals;

    /**
     * Creates a new game with a specified player, story, and a set of goals.
     * @param player The player of this game.
     * @param story The story of this game.
     * @param goals The goals specifying parameters of victory in this game.
     */
    public Game(Player player, Story story, List<Goal> goals) {
        this.player = player;
        this.story = story;
        this.goals = goals;
    }

    /**
     * Returns the main player in this game.
     * @return the main player in this game.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the story of this game.
     * @return the story of this game.
     */
    public Story getStory() {
        return story;
    }

    /**
     * Returns the goals currently in this game.
     * @return the goals currently in this game.
     */
    public List<Goal> getGoals() {
        return goals;
    }

    /**
     * Starts this game.
     * @return the initial passage of this game.
     */
    public Passage begin() {
        return story.getOpeningPassage();
    }

    /**
     * Uses a link object to progress the story of this game to a new passage.
     * @param link the Link-object pointing to a new passage.
     * @return The new passage referenced by the submitted Link-object.
     */
    public Passage go(Link link) {
        return story.getPassage(link);
    }
}
