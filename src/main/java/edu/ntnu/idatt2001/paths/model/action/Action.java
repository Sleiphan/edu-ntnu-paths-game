package edu.ntnu.idatt2001.paths.model.action;

import edu.ntnu.idatt2001.paths.model.Player;

import javax.script.ScriptEngine;

public interface Action {
    void execute(Player player);
    void execute(ScriptEngine engine);
}
