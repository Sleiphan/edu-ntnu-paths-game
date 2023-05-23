package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * A class for executing a change in a player objects gold field.
 */
public class GoldAction implements Action {
    public static final String SCRIPT_VARIABLE_KEY = "gold";

    private final int gold;
    private final int hashCode;

    /**
     * Initializes the value to change a player objects gold field by.
     *
     * @param gold amount to change gold field by
     */
    public GoldAction(int gold) {
        this.gold = gold;
        this.hashCode = this.getClass().hashCode() * gold < 0 ? gold : gold + 1;
    }

    /**
     * Executes the change to the player object given as a parameter's gold field.
     *
     * @param player player to change gold of
     */
    @Override
    public void execute(Player player) {
        player.changeGold(gold);
    }

    @Override
    public void execute(ScriptEngine engine) {
        String operator = gold >= 0 ? "+" : "-";

        try {
            engine.eval(SCRIPT_VARIABLE_KEY + operator + "=" + Math.abs(gold));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the gold value associated with the gold action.
     *
     * @return the gold value associated with the gold action
     */
    public int getGold() {
        return gold;
    }

    /**
     * Creates a hashCode for the GoldAction.
     *
     * @return hashCode for the GoldAction
     */
    @Override
    public int hashCode() {
        return hashCode;
    }
}
