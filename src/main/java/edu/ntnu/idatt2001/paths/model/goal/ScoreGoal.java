package edu.ntnu.idatt2001.paths.model.goal;

import edu.ntnu.idatt2001.paths.model.Player;

/**
 * Used to check if a player-object fulfills a minimum score requirement
 */
public class ScoreGoal implements GoalInfo {
    private final int minimumPoints;

    /**
     * Assigns a minimum score value
     * @param minimumPoints minimum allowed score value
     */
    public ScoreGoal(int minimumPoints){
        this.minimumPoints = minimumPoints;
    }

    /**
     * Checks if a player-object fulfills a minimum score requirement
     * @param player    player-object to check if fulfills the minimum requirement
     * @return          True if requirement met, false if not
     */
    @Override
    public boolean isFulfilled(Player player) {
        return player.getScore() >= minimumPoints;
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
        Object value = minimumPoints;
        return value;
    }
}
