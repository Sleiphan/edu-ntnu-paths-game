package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.goal.GoldGoal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GoldGoalTest {

    @Test
    void isFulfilled() {
        Player p = new Player("Test name", 1, 1, 1);
        GoldGoal testGoal = new GoldGoal(5);
        Assertions.assertFalse(testGoal.isFulfilled(p));
        p.addGold(5);
        Assertions.assertTrue(testGoal.isFulfilled(p));
    }
}