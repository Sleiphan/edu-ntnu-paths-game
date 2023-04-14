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
        //Player p = new Player("Test name", 1,1,1);
        player.addToInventory(item);
        return true;
    }

    /**
     * Converts the InventoryAction to a string that can be written to the .paths format
     * @return the InventoryAction as a string
     */
    public String toPathsFormat() {
        return "{inventoryAction:" + item +"}";
    }

    /**
     * Reads InventoryAction from a string
     * @param pathsString   The string to search for an inventory action.
     * @return              Null if no inventory action found, a new inventory action if
     *                      inventory action found
     */
    public static InventoryAction fromPathsFormat(String pathsString) {
        boolean checkIfInventoryAction = pathsString.contains("{inventoryAction:");
        if(checkIfInventoryAction){
            StringBuilder current = new StringBuilder();
            StringBuilder item = new StringBuilder();
            for(int i = 0; i < pathsString.length(); i++){
                if(current.toString().contains("{inventoryAction:")){
                    if(pathsString.charAt(i) == '}'){
                        return new InventoryAction(item.toString());
                    }
                    item.append(pathsString.charAt(i));
                }
                current.append(pathsString.charAt(i));
            }
        }
        return null;
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

