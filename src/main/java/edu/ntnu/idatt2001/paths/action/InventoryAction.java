package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing the addition of an item to the inventory array of a player object
 */
public class InventoryAction implements Action {

    private final String item;
    private final int hashCode;

    /**
     * Initializes the item that will be added to the inventory array of a player object
     * @param item item that will be added to the inventory array of a player object
     */
    public InventoryAction(String item){
        this.item = item;
        this.hashCode = this.getClass().hashCode() * item.hashCode();
    }

    /**
     * Adds the item to the inventory array of the player object given as a parameter.
     * @param player    edu.ntnu.idatt2001.paths.Player whose inventory will be added to
     * @return          True after item added
     */
    @Override
    public boolean execute(Player player) {
        player.addToInventory(item);
        return true;
    }

    /**
     * Gets the item string associated with the inventory action
     * @return the item string associated with the inventory action
     */
    public String getItem() {
        return item;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}

