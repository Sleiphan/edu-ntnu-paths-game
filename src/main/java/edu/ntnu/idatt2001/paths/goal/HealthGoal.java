package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;

/**
 * Used to check if a player-object fulfills a minimum health requirement
 */
public class HealthGoal implements Goal {
    private final int minimumHealth;

    /**
     * Assigns a minimum health value
     * @param minimumHealth minimum allowed health value
     */
    public HealthGoal(int minimumHealth){
        this.minimumHealth = minimumHealth;
    }

    /**
     * Checks if a player-object fulfills a minimum health requirement
     * @param player    player-object to check if fulfills the minimum requirement
     * @return          True if requirement met, false if not
     */
    @Override
    public boolean isFulfilled(Player player) {
        return player.getHealth() >= minimumHealth;
    }
}
