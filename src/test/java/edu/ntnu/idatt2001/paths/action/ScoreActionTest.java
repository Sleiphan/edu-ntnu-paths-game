package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.ScoreAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreActionTest {

    @Test
    void execute() {
        Player p = new Player.PlayerBuilder("Test name",1).setScore(1).setGold(1).build();
        ScoreAction testScoreAction = new ScoreAction(1);
        Assertions.assertTrue(testScoreAction.execute(p));
        ScoreAction testScoreActionNegative = new ScoreAction(-1);
        Assertions.assertTrue(testScoreActionNegative.execute(p));
        ScoreAction testScoreActionZero = new ScoreAction(0);
        Assertions.assertFalse(testScoreActionZero.execute(p));
    }

    @Test
    void toPathsFormat(){
        ScoreAction scoreAction = new ScoreAction(10);
        String scoreActionAsString = scoreAction.toPathsFormat();
        Assertions.assertEquals("{scoreAction:10}",scoreActionAsString);
        Assertions.assertNotEquals("{scoreAction:9}",scoreActionAsString);
    }

    @Test
    void fromPathsFormat(){
        String testString = "{inventoryAction:item} {scoreAction:10}";
        ScoreAction scoreAction = ScoreAction.fromPathsFormat(testString);
        Assertions.assertEquals(10, scoreAction.getPoints());
        Assertions.assertNotEquals(9,scoreAction.getPoints());
    }
}