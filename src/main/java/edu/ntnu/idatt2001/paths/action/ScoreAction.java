package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects points field
 */
public class ScoreAction implements Action{
    private final int points;

    /**
     * Initializes the value to change a player objects score field by
     * @param points amount to change player-objects score field by
     */
    public ScoreAction(int points){
        this.points = points;
    }

    /**
     * Executes the change to the player object given as a parameter's score field
     * @param player    player to change score of
     * @return          True if score changed, false if not
     */
    @Override
    public boolean execute(Player player) {
        Player p = new Player("Test name", 1,1,1);
        if(points > 0){
            p.addScore(points);
            return true;
        } else if (points < 0) {
            p.reduceScore(points);
            return true;
        }
        return false;
    }
}
