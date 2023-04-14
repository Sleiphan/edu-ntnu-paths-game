package edu.ntnu.idatt2001.paths;

import edu.ntnu.idatt2001.paths.goal.Goal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

    @Test
    void constructor() {
        Passage p = new Passage("Test title", "Test content", new ArrayList<>());
        Player player = new Player.PlayerBuilder("Balfor",100).setScore(0).setGold(10).build();
        Story story = new Story("", p);
        List<Goal> goals = new ArrayList<>();
        Game game = new Game(player, story, goals);

        Assertions.assertEquals(player, game.getPlayer());
        Assertions.assertEquals(story, game.getStory());
        Assertions.assertEquals(goals, game.getGoals());
    }

    @Test
    void begin() {
        Passage p = new Passage("Test title", "Test content", new ArrayList<>());
        Player player = new Player.PlayerBuilder("Balfor",100).setScore(0).setGold(10).build();
        Story story = new Story("", p);
        List<Goal> goals = new ArrayList<>();
        Game game = new Game(player, story, goals);

        Assertions.assertEquals(game.begin(), p);
    }

    @Test
    void go() {
        Link l = new Link("To next passage", "Next passage");
        List<Link> links1 = new ArrayList<>();
        links1.add(l);

        Passage initial = new Passage("Test title", "Test content", links1);
        Passage toPassage = new Passage("Next passage", "This is a new passage", new ArrayList<>());

        Player player = new Player.PlayerBuilder("Balfor",100).setScore(0).setGold(10).build();
        Story story = new Story("", initial);
        story.addPassage(toPassage);
        List<Goal> goals = new ArrayList<>();
        Game game = new Game(player, story, goals);

        Assertions.assertEquals(game.go(l), toPassage);
    }
}
