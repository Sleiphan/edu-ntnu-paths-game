package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;
import org.junit.jupiter.api.Test;

class GoldActionTest {

    @Test
    void execute() {
        Player p = new Player.PlayerBuilder("Test name",1).setScore(1).setGold(1).build();

        new GoldAction(1).execute(p);
        int gold_1 = p.getGold();
        new GoldAction(-1).execute(p);
        int gold_2 = p.getGold();
        new GoldAction(0).execute(p);
        int gold_3 = p.getGold();

        assert gold_1 == 2;
        assert gold_2 == 1;
        assert gold_3 == 1;
    }
}