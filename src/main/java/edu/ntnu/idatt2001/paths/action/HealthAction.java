package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects health field
 */
public class HealthAction implements Action{
    private final int health;

    /**
     * Initializes the value to change a player objects health field by
     * @param health amount to change a player objects health field by
     */
    public HealthAction(int health){
        this.health = health;
    }

    /**
     * Executes the change to the player object given as a parameter's health field
     * @param player    player to change health of
     * @return          True if health changed, false if not
     */
    @Override
    public boolean execute(Player player) {
        Player p = new Player("Test name", 1,1,1);
        if(health > 0){
            p.addHealth(health);
            return true;
        } else if (health < 0) {
            p.reduceHealth(health);
            return true;
        }
        return false;
    }

    public String toPathsFormat() {
        return null; // TODO: Implement
    }
    public static HealthAction fromPathsFormat(String pathsString) {
        return null; // TODO: Implement
    }
}
