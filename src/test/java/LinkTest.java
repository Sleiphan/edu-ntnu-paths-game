import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LinkTest {

    @Test
    public void link_class_equality_test() {
        String text = "Testing text";
        String reference = "Testing reference";

        Link l       = new Link(text, reference);
        Link equal   = new Link(text, reference);
        Link unequal = new Link(text, reference);
        unequal.addAction(new InventoryAction());

        assertEquals(text, l.getText());
        assertEquals(reference, l.getReference());
        assertEquals(l, equal);
        assertNotEquals(l, unequal);
    }

    @Test
    public void link_class_no_direct_access_to_internal_arraylist() {
        String text = "Testing text";
        String reference = "Testing reference";
        Link l = new Link(text, reference);

        l.addAction(new InventoryAction());
        List<Action> list = l.getActions();
        list.add(new InventoryAction());

        assertEquals(1, l.getActions().size());
    }
}
