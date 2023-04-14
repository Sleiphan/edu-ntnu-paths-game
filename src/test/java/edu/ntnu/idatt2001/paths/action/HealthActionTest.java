package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.HealthAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HandshakeCompletedEvent;

class HealthActionTest {

    @Test
    void execute() {
        Player p = new Player.PlayerBuilder("Test name",1).setScore(1).setGold(1).build();
        HealthAction testHealthAction = new HealthAction(1);
        Assertions.assertTrue(testHealthAction.execute(p));
        HealthAction testHealthActionNegative = new HealthAction(-1);
        Assertions.assertTrue(testHealthActionNegative.execute(p));
        HealthAction testHealthActionZero = new HealthAction(0);
        Assertions.assertFalse(testHealthActionZero.execute(p));
    }

    @Test
    void toPathsFormat(){
        HealthAction healthAction = new HealthAction(8);
        String healthActionAsString = healthAction.toPathsFormat();
        Assertions.assertEquals("{healthAction:8}",healthActionAsString);
        Assertions.assertNotEquals("{healthAction:7}",healthActionAsString);
    }

    @Test
    void fromPathsFormat(){
        String testString = "{healthAction:9} {goldAction:10}";
        HealthAction healthAction = HealthAction.fromPathsFormat(testString);
        Assertions.assertEquals(9,healthAction.getHealth());
        Assertions.assertNotEquals(10,healthAction.getHealth());
    }
}