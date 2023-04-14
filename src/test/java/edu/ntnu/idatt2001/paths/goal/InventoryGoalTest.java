package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.goal.InventoryGoal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InventoryGoalTest {

    @Test
    void isFulfilled() {
        List<String> mandatoryTest = new ArrayList<>();
        mandatoryTest.add("TestElement1");
        mandatoryTest.add("TestElement2");
        mandatoryTest.add("TestElement3");
        List<String> testInventory = new ArrayList<>();
        Player p = new Player.PlayerBuilder("Test name",1).setScore(1).setGold(1).setInventory(testInventory).build();
        InventoryGoal testGoal = new InventoryGoal(mandatoryTest);
        p.addToInventory("TestElement1");
        p.addToInventory("TestElement2");
        Assertions.assertFalse(testGoal.isFulfilled(p));
        p.addToInventory("TestElement3");
        Assertions.assertTrue(testGoal.isFulfilled(p));
    }
}