package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

import javax.script.ScriptEngine;

public interface Action {
    void execute(Player player);
    void execute(ScriptEngine engine);
}
