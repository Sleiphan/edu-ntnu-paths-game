package edu.ntnu.idatt2001.paths;

import edu.ntnu.idatt2001.paths.action.Action;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LinkTest {

    @Test
    public void equals_method_test() {
        String text = "Testing text";
        String reference = "Testing reference";
        String item = "Testing item";

        Link l       = new Link(text, reference);
        Link equal   = new Link(text, reference);
        Link unequal = new Link(text, reference);
        unequal.addAction(new InventoryAction(item));

        Assertions.assertEquals(text, l.getText());
        Assertions.assertEquals(reference, l.getReference());
        Assertions.assertEquals(l, equal);
        Assertions.assertNotEquals(l, unequal);
    }
    public boolean has_direct_access_to_internal_arraylist() {
        String text = "Testing text";
        String reference = "Testing reference";
        String item = "Testing item";
        Link l = new Link(text, reference);

        l.addAction(new InventoryAction(item));
        List<Action> list = l.getActions();
        list.add(new InventoryAction(item));

        return l.getActions().size() != 1;
    }

    @Test
    public void direct_access_to_internal_arraylist() {
        assert(has_direct_access_to_internal_arraylist());
    }

    public void no_direct_access_to_internal_arraylist() {
        assertFalse(has_direct_access_to_internal_arraylist());
    }

    @Test
    public void hashcode_recalculates_correctly() {
        String text = "Testing text";
        String reference = "Testing reference";
        String item = "Testing item";
        Link l = new Link(text, reference);

        int old_hash = l.hashCode();
        int new_hash = l.hashCode();
        assertEquals(old_hash, new_hash);

        l.addAction(new InventoryAction(item));
        new_hash = l.hashCode();
        assertNotEquals(old_hash, new_hash);
    }

    @Test
    public void constructor() {
        assertThrows(IllegalArgumentException.class, () -> new Link(null, ""));
        assertThrows(IllegalArgumentException.class, () -> new Link("", null));
        assertDoesNotThrow(() -> new Link("", ""));

        String text = "Testing text";
        String reference = "Testing reference";
        Link l = new Link(text, reference);

        Assertions.assertEquals(l.getText(), text);
        Assertions.assertEquals(l.getReference(), reference);
        Assertions.assertEquals(l.getActions().size(), 0);
    }
}
