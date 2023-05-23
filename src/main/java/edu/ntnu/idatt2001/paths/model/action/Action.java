package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;

import javax.script.ScriptEngine;

public interface Action {

    /**
     * Performs this action on the specified player.
     *
     * @param player The player performing the action.
     */
    void execute(Player player);

    /**
     * Performs any script-relevant actions on the specified script engine.
     *
     * @param engine The engine to perform this action on.
     */
    void execute(ScriptEngine engine);
}
