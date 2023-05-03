package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects points field
 */
public class ScoreAction implements Action{
    private final int points;
    private final int hashCode;

    /**
     * Initializes the value to change a player objects score field by
     * @param points amount to change player-objects score field by
     */
    public ScoreAction(int points){
        this.points = points;
        this.hashCode = GoldAction.class.hashCode() * points < 0 ? points : points + 1;
    }

    /**
     * Executes the change to the player object given as a parameter's score field
     * @param player    player to change score of
     * @return          True if score changed, false if not
     */
    @Override
    public boolean execute(Player player) {
        //Player p = new Player("Test name", 1,1,1);
        if(points > 0){
            player.addScore(points);
            return true;
        } else if (points < 0) {
            player.reduceScore(points);
            return true;
        }
        return false;
    }

    /**
     * Gets the score value associated with the score action
     * @return the score value associated with the score action
     */
    public int getPoints() {
        return points;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
