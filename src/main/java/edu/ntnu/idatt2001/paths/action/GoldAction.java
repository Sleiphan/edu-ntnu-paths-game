package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects gold field
 */
public class GoldAction implements Action{
    private final int gold;
    private final int hashCode;

    /**
     * Initializes the value to change a player objects gold field by
     * @param gold amount to change gold field by
     */
    public GoldAction(int gold){
        this.gold = gold;
        this.hashCode = this.getClass().hashCode() * gold < 0 ? gold : gold + 1;
    }

    /**
     * Executes the change to the player object given as a parameter's gold field
     * @param player    player to change gold of
     * @return          True if gold changed, false if not
     */
    @Override
    public boolean execute(Player player) {
        //Player p = new Player("Test name", 1,1,1);
        if(gold > 0){
            player.addGold(gold);
            return true;
        } else if (gold < 0) {
            player.reduceGold(gold);
            return true;
        }
        return false;
    }

    /**
     * Converts the GoldAction to string that can be written to the .paths format
     * @return the GoldAction as a string
     */
    public String toPathsFormat() {
        return "{goldAction:" + gold +"}";
    }

    /**
     * Reads GoldAction from a string.
     * @param pathsString The string to search for gold actions
     * @return            Null if no gold action found, a new gold action if gold action found.
     */
    public static GoldAction fromPathsFormat(String pathsString) {
        boolean checkIfGoldAction = pathsString.contains("{goldAction:");
        if(checkIfGoldAction){
            StringBuilder current = new StringBuilder();
            StringBuilder gold = new StringBuilder();
            for(int i = 0; i < pathsString.length(); i++){
                if(current.toString().contains("{goldAction:")){
                    if(pathsString.charAt(i) == '}'){
                        return new GoldAction(Integer.parseInt(gold.toString()));
                    }
                    gold.append(pathsString.charAt(i));
                }
                current.append(pathsString.charAt(i));
            }
        }
        return null;
    }

    /**
     * Gets the gold value associated with the gold action
     * @return the gold value associated with the gold action
     */
    public int getGold() {
        return gold;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
