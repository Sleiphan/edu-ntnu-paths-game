import java.util.List;
import java.util.Objects;

/**
 * Used to check if a player-object has certain items in their inventory
 */
public class InventoryGoal implements Goal{
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
}
