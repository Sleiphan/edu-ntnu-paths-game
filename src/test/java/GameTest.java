import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

    @Test
    void constructor() {
        Passage p = new Passage("Test title", "Test content", new ArrayList<>());
        Player player = new Player("Balfor", 100, 0, 10);
        Story story = new Story("", p);
        List<Goal> goals = new ArrayList<>();
        Game game = new Game(player, story, goals);

        assertEquals(player, game.getPlayer());
        assertEquals(story, game.getStory());
        assertEquals(goals, game.getGoals());
    }

    @Test
    void begin() {
        Passage p = new Passage("Test title", "Test content", new ArrayList<>());
        Player player = new Player("Balfor", 100, 0, 10);
        Story story = new Story("", p);
        List<Goal> goals = new ArrayList<>();
        Game game = new Game(player, story, goals);

        assertEquals(game.begin(), p);
    }

    @Test
    void go() {
        Link l = new Link("To next passage", "Next passage");
        List<Link> links1 = new ArrayList<>();
        links1.add(l);

        Passage initial = new Passage("Test title", "Test content", links1);
        Passage toPassage = new Passage("Next passage", "This is a new passage", new ArrayList<>());

        Player player = new Player("Balfor", 100, 0, 10);
        Story story = new Story("", initial);
        story.addPassage(toPassage);
        List<Goal> goals = new ArrayList<>();
        Game game = new Game(player, story, goals);

        assertEquals(game.go(l), toPassage);
    }
}
