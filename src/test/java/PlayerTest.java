import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void addHealth() {
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;

        Player p = new Player(name, testHealth, testScore, testGold);

        int testHealthPos = 1;
        int testHealthZero = 0;
        int testHealthNeg = -1;

        assertTrue(p.addHealth(testHealthPos));
        assertFalse(p.addHealth(testHealthZero));
        assertFalse(p.addHealth(testHealthNeg));
    }

    @Test
    void reduceHealth(){
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;

        Player p = new Player(name, testHealth, testScore, testGold);

        int testHealthPos = 1;
        int testHealthZero = 0;
        int testHealthNeg = -1;

        assertFalse(p.reduceHealth(testHealthPos));
        assertFalse(p.reduceHealth(testHealthZero));
        assertTrue(p.reduceHealth(testHealthNeg));
    }

    @Test
    void addScore() {
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;

        Player p = new Player(name, testHealth, testScore, testGold);

        int testScorePos = 1;
        int testScoreZero = 0;
        int testScoreNeg = -1;

        assertTrue(p.addScore(testScorePos));
        assertFalse(p.addScore(testScoreZero));
        assertFalse(p.addScore(testScoreNeg));
    }

    @Test
    void reduceScore(){
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;

        Player p = new Player(name, testHealth, testScore, testGold);

        int testScorePos = 1;
        int testScoreZero = 0;
        int testScoreNeg = -1;

        assertFalse(p.reduceScore(testScorePos));
        assertFalse(p.reduceScore(testScoreZero));
        assertTrue(p.reduceScore(testScoreNeg));
    }

    @Test
    void addGold() {
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;

        Player p = new Player(name, testHealth, testScore, testGold);

        int testGoldPos = 1;
        int testGoldZero = 0;
        int testGoldNeg = -1;

        assertTrue(p.addGold(testGoldPos));
        assertFalse(p.addGold(testGoldZero));
        assertFalse(p.addGold(testGoldNeg));
    }

    @Test
    void reduceGold() {
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;

        Player p = new Player(name, testHealth, testScore, testGold);

        int testGoldPos = 1;
        int testGoldZero = 0;
        int testGoldNeg = -1;

        assertFalse(p.reduceGold(testGoldPos));
        assertFalse(p.reduceGold(testGoldZero));
        assertTrue(p.reduceGold(testGoldNeg));
    }

    @Test
    void addToInventory() {
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;
        String item = "Test item";

        Player p = new Player(name, testHealth, testScore, testGold);

        assertTrue(p.addToInventory(item));
    }

    @Test
    void removeFromInventory(){
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;
        String item = "Test item";

        Player p = new Player(name, testHealth, testScore, testGold);

        assertFalse(p.removeFromInventory(item));
        p.addToInventory(item);
        assertTrue(p.removeFromInventory(item));
    }
}