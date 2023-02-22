import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthActionTest {

    @Test
    void execute() {
        Player p = new Player("Test name", 1,1,1);
        HealthAction testHealthAction = new HealthAction(1);
        assertTrue(testHealthAction.execute(p));
        HealthAction testHealthActionNegative = new HealthAction(-1);
        assertTrue(testHealthActionNegative.execute(p));
        HealthAction testHealthActionZero = new HealthAction(0);
        assertFalse(testHealthActionZero.execute(p));
    }
}