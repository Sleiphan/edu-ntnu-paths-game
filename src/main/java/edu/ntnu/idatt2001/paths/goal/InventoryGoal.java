package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;

import java.util.List;

/**
 * Used to check if a player-object has certain items in their inventory
 */
public class InventoryGoal implements GoalInfo {
    private final List<String> mandatoryItems;

    /**
     * Assigns a mandatory array of items
     * @param mandatoryItems array of mandatory items
     */
    public InventoryGoal(List<String> mandatoryItems){
        this.mandatoryItems = mandatoryItems;
    }

    /**
     * Checks if a player-object fulfills a mandatory list of items
     * @param player    player-object to check if it has the mandatory list of items
     * @return          True if requirement met, false if not
     */
    @Override
    public boolean isFulfilled(Player player) {
        if (mandatoryItems.size() > player.getInventory().size()){
            return false;
        }
        for (String mandatoryItem : mandatoryItems) {
            if (!player.getInventory().contains(mandatoryItem)) {
                return false;
            }
        }
        return true;
    }

    public String getType() {
        String type = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
        return type;
    }

    @Override
    public Object getValue() {
        Object value = mandatoryItems;
        return value;
    }
}
