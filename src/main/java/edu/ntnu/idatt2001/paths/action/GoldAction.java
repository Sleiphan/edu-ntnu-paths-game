package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects gold field
 */
public class GoldAction implements Action{
    private final int gold;

    /**
     * Initializes the value to change a player objects gold field by
     * @param gold amount to change gold field by
     */
    public GoldAction(int gold){
        this.gold = gold;
    }

    /**
     * Executes the change to the player object given as a parameter's gold field
     * @param player    player to change gold of
     * @return          True if gold changed, false if not
     */
    @Override
    public boolean execute(Player player) {
        Player p = new Player("Test name", 1,1,1);
        if(gold > 0){
            p.addGold(gold);
            return true;
        } else if (gold < 0) {
            p.reduceGold(gold);
            return true;
        }
        return false;
    }

    public String toPathsFormat() {
        return null; // TODO: Implement
    }
    public static GoldAction fromPathsFormat(String pathsString) {
        return null; // TODO: Implement
    }
}
