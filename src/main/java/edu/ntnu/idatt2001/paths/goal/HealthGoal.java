package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;

/**
 * Used to check if a player-object fulfills a minimum health requirement
 */
public class HealthGoal implements GoalInfo {
    private final int minimumHealth;
    private final String type = "Health";

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

    /**
     * Gets the type of the action represented as a string
     * @return the type of the action represented as a string
     */
    @Override
    public String getType() {
        String type = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
        return type;
    }

    /**
     * Gets the value associated with the action
     * @return the value associated with the action
     */
    @Override
    public Object getValue() {
        Object value = minimumHealth;
        return value;
    }
}
