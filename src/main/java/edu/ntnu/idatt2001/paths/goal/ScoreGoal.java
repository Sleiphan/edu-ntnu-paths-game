package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;

/**
 * Used to check if a player-object fulfills a minimum score requirement
 */
public class ScoreGoal implements Goal, GoalInfo {
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

    @Override
    public String getType() {
        String type = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
        return type;
    }

    @Override
    public Object getValue() {
        Object value = minimumPoints;
        return value;
    }
}
