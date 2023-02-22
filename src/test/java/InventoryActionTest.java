import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        InventoryAction testInventoryAction = new InventoryAction("Test item");
        assertTrue(testInventoryAction.execute(p));
    }
}