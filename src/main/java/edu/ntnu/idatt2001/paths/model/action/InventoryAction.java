package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * A class for executing the addition of an item to the inventory array of a player object.
 */
public class InventoryAction implements Action {

    private final String item;
    private final int hashCode;
    private final boolean add;

    /**
     * Initializes the item that will be added to the inventory array of a player object.
     *
     * @param item item that will be added to the inventory array of a player object
     */
    public InventoryAction(String item) {
        this(item, true);
    }

    /**
     * Sets the item to either add or consume with this action.
     *
     * @param item The item that will be added to the inventory array of a player object
     * @param add  Whether this action will add or consume the item specified in
     *             the <code>item</code>-argument.
     */
    public InventoryAction(String item, boolean add) {
        this.item = item;
        this.add = add;
        this.hashCode = this.getClass().hashCode() * item.hashCode() * (add ? 1 : 2);
    }

    /**
     * Performs this item action on the inventory of the player-argument.
     *
     * @param player Player whose inventory will be changed.
     */
    @Override
    public void execute(Player player) {
        if (add)
            player.addToInventory(item);
        else
            player.removeFromInventory(item);
    }

    @Override
    public void execute(ScriptEngine engine) {
        if (engine.getContext().getAttribute(item) == null)
            engine.put(item, 0);

        String operator = "+";
        if (!addsItem())
            operator = "-";

        try {
            engine.eval(item + operator + "=1");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the item string associated with the inventory action.
     *
     * @return the item string associated with the inventory action
     */
    public String getItem() {
        return item;
    }

    /**
     * Indicates whether this action adds or consumes an item.
     *
     * @return <code>true</code> if this action adds an item to a players inventory.
     *      Otherwise, returns false.
     */
    public boolean addsItem() {
        return add;
    }

    /**
     * Creates a hashCode for the inventoryAction.
     *
     * @return hashCode for the InventoryAction
     */
    @Override
    public int hashCode() {
        return hashCode;
    }
}

