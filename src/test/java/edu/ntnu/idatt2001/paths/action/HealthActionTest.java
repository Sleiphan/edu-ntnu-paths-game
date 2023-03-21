package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.HealthAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        HealthAction testHealthAction = new HealthAction(1);
        Assertions.assertTrue(testHealthAction.execute(p));
        HealthAction testHealthActionNegative = new HealthAction(-1);
        Assertions.assertTrue(testHealthActionNegative.execute(p));
        HealthAction testHealthActionZero = new HealthAction(0);
        Assertions.assertFalse(testHealthActionZero.execute(p));
    }
}