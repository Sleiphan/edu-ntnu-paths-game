package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.ScoreAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        ScoreAction testScoreAction = new ScoreAction(1);
        Assertions.assertTrue(testScoreAction.execute(p));
        ScoreAction testScoreActionNegative = new ScoreAction(-1);
        Assertions.assertTrue(testScoreActionNegative.execute(p));
        ScoreAction testScoreActionZero = new ScoreAction(0);
        Assertions.assertFalse(testScoreActionZero.execute(p));
    }
}