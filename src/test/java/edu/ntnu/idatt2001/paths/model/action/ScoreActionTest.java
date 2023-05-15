package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;
import org.junit.jupiter.api.Test;

class ScoreActionTest {

    @Test
    void execute() {
        Player p = new Player.PlayerBuilder("Test name",1).setScore(1).setGold(1).build();

        new ScoreAction(1).execute(p);
        int score_1 = p.getScore();
        new ScoreAction(-1).execute(p);
        int score_2 = p.getScore();
        new ScoreAction(0).execute(p);
        int score_3 = p.getScore();

        assert score_1 == 2;
        assert score_2 == 1;
        assert score_3 == 1;
    }
}