package edu.ntnu.idatt2001.paths;

import org.junit.jupiter.api.Assertions;
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

        Assertions.assertTrue(p.addHealth(testHealthPos));
        Assertions.assertFalse(p.addHealth(testHealthZero));
        Assertions.assertFalse(p.addHealth(testHealthNeg));
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

        Assertions.assertFalse(p.reduceHealth(testHealthPos));
        Assertions.assertFalse(p.reduceHealth(testHealthZero));
        Assertions.assertTrue(p.reduceHealth(testHealthNeg));
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

        Assertions.assertTrue(p.addScore(testScorePos));
        Assertions.assertFalse(p.addScore(testScoreZero));
        Assertions.assertFalse(p.addScore(testScoreNeg));
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

        Assertions.assertFalse(p.reduceScore(testScorePos));
        Assertions.assertFalse(p.reduceScore(testScoreZero));
        Assertions.assertTrue(p.reduceScore(testScoreNeg));
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

        Assertions.assertTrue(p.addGold(testGoldPos));
        Assertions.assertFalse(p.addGold(testGoldZero));
        Assertions.assertFalse(p.addGold(testGoldNeg));
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

        Assertions.assertFalse(p.reduceGold(testGoldPos));
        Assertions.assertFalse(p.reduceGold(testGoldZero));
        Assertions.assertTrue(p.reduceGold(testGoldNeg));
    }

    @Test
    void addToInventory() {
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;
        String item = "Test item";

        Player p = new Player(name, testHealth, testScore, testGold);

        Assertions.assertTrue(p.addToInventory(item));
    }

    @Test
    void removeFromInventory(){
        String name = "Test name";
        int testHealth = 1;
        int testScore = 1;
        int testGold = 1;
        String item = "Test item";

        Player p = new Player(name, testHealth, testScore, testGold);

        Assertions.assertFalse(p.removeFromInventory(item));
        p.addToInventory(item);
        Assertions.assertTrue(p.removeFromInventory(item));
    }
}