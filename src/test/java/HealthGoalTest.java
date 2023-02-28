import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthGoalTest {

    @Test
    void isFulfilled() {
        Player p = new Player("Test name", 1, 1, 1);
        HealthGoal testGoal = new HealthGoal(5);
        assertFalse(testGoal.isFulfilled(p));
        p.addHealth(5);
        assertTrue(testGoal.isFulfilled(p));
    }
}