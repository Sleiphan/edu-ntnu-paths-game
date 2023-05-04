package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InventoryActionTest {

    @Test
    void execute() {
        final String item = "Test item";
        Player p = new Player.PlayerBuilder("Test name",1).build();

        new InventoryAction(item).execute(p);

        assert p.getInventory().contains(item);
    }
}