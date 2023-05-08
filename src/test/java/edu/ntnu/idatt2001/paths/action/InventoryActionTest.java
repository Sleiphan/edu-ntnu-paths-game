package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InventoryActionTest {

    @Test
    void add_item() {
        final String item = "Test item";
        Player p = new Player.PlayerBuilder("Test name",1).build();

        new InventoryAction(item).execute(p);

        assert p.hasItem(item);
    }

    @Test
    void remove_item() {
        final String item1 = "Test item 1";
        final String item2 = "Test item 2";
        Player p = new Player.PlayerBuilder("Test name",1).build();

        new InventoryAction(item1, true).execute(p);
        new InventoryAction(item2, true).execute(p);
        new InventoryAction(item2, false).execute(p);

        assert p.hasItem(item1);
        assert !p.hasItem(item2);
    }
}