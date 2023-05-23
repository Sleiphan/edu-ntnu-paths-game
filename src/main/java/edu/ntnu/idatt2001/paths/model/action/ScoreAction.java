package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * A class for executing a change in a player objects points field.
 */
public class ScoreAction implements Action {
    public static final String SCRIPT_VARIABLE_KEY = "score";
    private final int points;
    private final int hashCode;

    /**
     * Initializes the value to change a player objects score field by.
     *
     * @param points amount to change player-objects score field by
     */
    public ScoreAction(int points) {
        this.points = points;
        this.hashCode = GoldAction.class.hashCode() * points < 0 ? points : points + 1;
    }

    /**
     * Executes the change to the player object given as a parameter's score field.
     *
     * @param player player to change score of
     */
    @Override
    public void execute(Player player) {
        player.changeScore(points);
    }

    @Override
    public void execute(ScriptEngine engine) {
        String operator = points >= 0 ? "+" : "-";

        try {
            engine.eval(SCRIPT_VARIABLE_KEY + operator + "=" + Math.abs(points));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the score value associated with the score action.
     *
     * @return the score value associated with the score action
     */
    public int getPoints() {
        return points;
    }

    /**
     * Creates a hashCode for the ScoreAction.
     *
     * @return hashCode for the ScoreAction
     */
    @Override
    public int hashCode() {
        return hashCode;
    }
}
