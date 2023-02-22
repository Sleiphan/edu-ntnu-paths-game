import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        GoldAction testGoldAction = new GoldAction(1);
        assertTrue(testGoldAction.execute(p));
        GoldAction testGoldActionNegative = new GoldAction(-1);
        assertTrue(testGoldActionNegative.execute(p));
        GoldAction testGoldActionZero = new GoldAction(0);
        assertFalse(testGoldActionZero.execute(p));
    }
}