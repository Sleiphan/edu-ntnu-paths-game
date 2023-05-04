package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects health field
 */
public class HealthAction implements Action{
    private final int health;
    private final int hashCode;

    /**
     * Initializes the value to change a player objects health field by
     * @param health amount to change a player objects health field by
     */
    public HealthAction(int health){
        this.health = health;
        this.hashCode = this.getClass().hashCode() * health < 0 ? health : health + 1;
    }

    /**
     * Executes the change to the player object given as a parameter's health field
     * @param player player to change health of
     */
    @Override
    public void execute(Player player) {
        player.changeHealth(health);
    }

    /**
     * Gets the health value associated with the health action
     * @return the health value associated with the health action
     */
    public int getHealth() {
        return health;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
