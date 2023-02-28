import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        ScoreAction testScoreAction = new ScoreAction(1);
        assertTrue(testScoreAction.execute(p));
        ScoreAction testScoreActionNegative = new ScoreAction(-1);
        assertTrue(testScoreActionNegative.execute(p));
        ScoreAction testScoreActionZero = new ScoreAction(0);
        assertFalse(testScoreActionZero.execute(p));
    }
}