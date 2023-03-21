package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InventoryActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        InventoryAction testInventoryAction = new InventoryAction("Test item");
        Assertions.assertTrue(testInventoryAction.execute(p));
    }
}