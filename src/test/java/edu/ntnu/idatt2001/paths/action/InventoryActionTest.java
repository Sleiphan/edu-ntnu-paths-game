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

    @Test
    void toPathsFormat(){
        InventoryAction inventoryAction = new InventoryAction("test");
        String inventoryActionAsString = inventoryAction.toPathsFormat();
        Assertions.assertEquals("{inventoryAction:test}",inventoryActionAsString);
        Assertions.assertNotEquals("{inventoryAction:false}",inventoryActionAsString);
    }

    @Test
    void fromPathsFormat(){
        String testString = "{scoreAction:10} {inventoryAction:item}";
        InventoryAction inventoryAction = InventoryAction.fromPathsFormat(testString);
        Assertions.assertEquals("item",inventoryAction.getItem());
        Assertions.assertNotEquals("10",inventoryAction.getItem());
    }
}