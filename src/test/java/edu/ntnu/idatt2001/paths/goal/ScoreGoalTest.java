package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.goal.ScoreGoal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreGoalTest {

    @Test
    void isFulfilled() {
        Player p = new Player("Test name", 1, 1, 1);
        ScoreGoal testGoal = new ScoreGoal(5);
        Assertions.assertFalse(testGoal.isFulfilled(p));
        p.addScore(5);
        Assertions.assertTrue(testGoal.isFulfilled(p));
    }
}