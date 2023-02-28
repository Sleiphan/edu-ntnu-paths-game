import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryGoalTest {

    @Test
    void isFulfilled() {
        Player p = new Player("Test name", 1, 1, 1);
        List<String> mandatoryTest = new ArrayList<>();
        mandatoryTest.add("TestElement1");
        mandatoryTest.add("TestElement2");
        mandatoryTest.add("TestElement3");
        InventoryGoal testGoal = new InventoryGoal(mandatoryTest);
        p.addToInventory("TestElement1");
        p.addToInventory("TestElement2");
        assertFalse(testGoal.isFulfilled(p));
        p.addToInventory("TestElement3");
        assertTrue(testGoal.isFulfilled(p));
    }
}