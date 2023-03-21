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
}