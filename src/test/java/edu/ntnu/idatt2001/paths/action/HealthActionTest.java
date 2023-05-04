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

        new HealthAction(1).execute(p);
        int health_1 = p.getHealth();
        new HealthAction(-1).execute(p);
        int health_2 = p.getHealth();
        new HealthAction(0).execute(p);
        int health_3 = p.getHealth();

        assert health_1 == 2;
        assert health_2 == 1;
        assert health_3 == 1;
    }
}