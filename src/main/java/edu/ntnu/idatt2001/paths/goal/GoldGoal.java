package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;

/**
 * Used to check if a player-object fulfills a minimum gold requirement
 */
public class GoldGoal implements Goal, GoalInfo{
    private final int minimumGold;

    /**
     * Assigns a minimum gold value
     * @param minimumGold minimum allowed gold value
     */
    public GoldGoal(int minimumGold){
        this.minimumGold = minimumGold;
    }

    /**
     * Checks if a player-object fulfills a minimum gold requirement
     * @param player    player-object to check if fulfills the minimum requirement
     * @return          True if requirement met, false if not
     */
    @Override
    public boolean isFulfilled(Player player) {
        return player.getGold() >= minimumGold;
    }

    @Override
    public String getType() {
        String type = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
        return type;
    }

    @Override
    public Object getValue() {
        Object value = minimumGold;
        return value;
    }
}
