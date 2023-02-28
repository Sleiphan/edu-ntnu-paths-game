import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldGoalTest {

    @Test
    void isFulfilled() {
        Player p = new Player("Test name", 1, 1, 1);
        GoldGoal testGoal = new GoldGoal(5);
        assertFalse(testGoal.isFulfilled(p));
        p.addGold(5);
        assertTrue(testGoal.isFulfilled(p));
    }
}