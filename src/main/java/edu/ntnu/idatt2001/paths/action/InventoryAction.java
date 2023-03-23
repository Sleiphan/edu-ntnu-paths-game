package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing the addition of an item to the inventory array of a player object
 */
public class InventoryAction implements Action {

    private final String item;

    /**
     * Initializes the item that will be added to the inventory array of a player object
     * @param item item that will be added to the inventory array of a player object
     */
    public InventoryAction(String item){
        this.item = item;
    }

    /**
     * Adds the item to the inventory array of the player object given as a parameter.
     * @param player    edu.ntnu.idatt2001.paths.Player whose inventory will be added to
     * @return          True after item added
     */
    @Override
    public boolean execute(Player player) {
        Player p = new Player("Test name", 1,1,1);
        p.addToInventory(item);
        return true;
    }

    public String toPathsFormat() {
        return null; // TODO: Implement
    }
    public static InventoryAction fromPathsFormat(String pathsString) {
        return null; // TODO: Implement
    }
}

