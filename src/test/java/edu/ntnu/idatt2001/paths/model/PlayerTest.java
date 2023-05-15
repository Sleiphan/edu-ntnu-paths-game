package edu.ntnu.idatt2001.paths.model;

import edu.ntnu.idatt2001.paths.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void changeHealth() {
        Player p = new Player.PlayerBuilder("Test name",1).setGold(1).setScore(1).build();

        p.changeHealth(1);
        int health_1 = p.getHealth();
        p.changeHealth(0);
        int health_2 = p.getHealth();
        p.changeHealth(-1);
        int health_3 = p.getHealth();

        assert health_1 == 2;
        assert health_2 == 2;
        assert health_3 == 1;
    }

    @Test
    void changeScore() {
        Player p = new Player.PlayerBuilder("Test name",1).setGold(1).setScore(1).build();

        p.changeScore(1);
        int score_1 = p.getScore();
        p.changeScore(0);
        int score_2 = p.getScore();
        p.changeScore(-1);
        int score_3 = p.getScore();

        assert score_1 == 2;
        assert score_2 == 2;
        assert score_3 == 1;
    }

    @Test
    void changeGold() {
        Player p = new Player.PlayerBuilder("Test name",1).setGold(1).setScore(1).build();

        p.changeGold(1);
        int gold_1 = p.getGold();
        p.changeGold(0);
        int gold_2 = p.getGold();
        p.changeGold(-1);
        int gold_3 = p.getGold();

        assert gold_1 == 2;
        assert gold_2 == 2;
        assert gold_3 == 1;
    }

    @Test
    void addToInventory() {
        String item = "Test item";
        Player p = new Player.PlayerBuilder("Test name",1).setGold(1).setScore(1).build();

        p.addToInventory(item);

        assert p.getInventory().contains(item);
    }

    @Test
    void removeFromInventory(){
        String item = "Test item";
        Player p = new Player.PlayerBuilder("Test name",1).setGold(1).setScore(1).build();

        p.addToInventory(item);
        p.removeFromInventory(item);

        assert !p.getInventory().contains(item);
    }

    @Test
    void playerBuilderTest(){
        Player player = new Player.PlayerBuilder("Test",2).setGold(3).setScore(4).build();
        Assertions.assertEquals(player.getName(),"Test");
        Assertions.assertEquals(player.getHealth(),2);
        Assertions.assertEquals(player.getScore(),4);
        Assertions.assertEquals(player.getGold(),3);
        assert player.getInventory() != null;
    }
}