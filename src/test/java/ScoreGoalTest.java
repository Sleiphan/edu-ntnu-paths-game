import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreGoalTest {

    @Test
    void isFulfilled() {
        Player p = new Player("Test name", 1, 1, 1);
        ScoreGoal testGoal = new ScoreGoal(5);
        assertFalse(testGoal.isFulfilled(p));
        p.addScore(5);
        assertTrue(testGoal.isFulfilled(p));
    }
}