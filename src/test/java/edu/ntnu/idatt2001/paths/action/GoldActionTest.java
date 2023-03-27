package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.GoldAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GoldActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        GoldAction testGoldAction = new GoldAction(1);
        Assertions.assertTrue(testGoldAction.execute(p));
        GoldAction testGoldActionNegative = new GoldAction(-1);
        Assertions.assertTrue(testGoldActionNegative.execute(p));
        GoldAction testGoldActionZero = new GoldAction(0);
        Assertions.assertFalse(testGoldActionZero.execute(p));
    }

    @Test
    void toPathsFormat(){
        GoldAction goldAction = new GoldAction(8);
        String goldActionAsString = goldAction.toPathsFormat();
        Assertions.assertEquals("{goldAction:8}",goldActionAsString);
        Assertions.assertNotEquals("{goldAction:7}",goldActionAsString);
    }

    @Test
    void fromPathsFormat(){
        String testString = "{healthAction:9} {goldAction:10}";
        GoldAction goldAction = GoldAction.fromPathsFormat(testString);
        Assertions.assertEquals(10,goldAction.getGold());
        Assertions.assertNotEquals(9,goldAction.getGold());

    }
}