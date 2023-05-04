package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects gold field
 */
public class GoldAction implements Action{
    private final int gold;
    private final int hashCode;

    /**
     * Initializes the value to change a player objects gold field by
     * @param gold amount to change gold field by
     */
    public GoldAction(int gold){
        this.gold = gold;
        this.hashCode = this.getClass().hashCode() * gold < 0 ? gold : gold + 1;
    }

    /**
     * Executes the change to the player object given as a parameter's gold field
     * @param player    player to change gold of
     * @return          True if gold changed, false if not
     */
    @Override
    public void execute(Player player) {
        player.changeGold(gold);
    }

    /**
     * Gets the gold value associated with the gold action
     * @return the gold value associated with the gold action
     */
    public int getGold() {
        return gold;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
