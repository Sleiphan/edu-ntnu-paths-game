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

    /**
     * Converts the ScoreAction to a string that can be written to the .paths format
     * @return the ScoreAction as a string
     */
    public String toPathsFormat() {
        return "{scoreAction:" + points +"}";
    }

    /**
     * Reads ScoreAction from a string
     * @param pathsString   The string to search for score actions
     * @return              Null if no score action found, a new score action if score action found
     */
    public static ScoreAction fromPathsFormat(String pathsString) {
        boolean checkIfScoreAction = pathsString.contains("{scoreAction:");
        if(checkIfScoreAction){
            StringBuilder current = new StringBuilder();
            StringBuilder score = new StringBuilder();
            for(int i = 0; i < pathsString.length(); i++){
                if(current.toString().contains("{scoreAction:")){
                    if(pathsString.charAt(i) == '}'){
                        return new ScoreAction(Integer.parseInt(score.toString()));
                    }
                    score.append(pathsString.charAt(i));
                }
                current.append(pathsString.charAt(i));
            }
        }
        return null;
    }

    /**
     * Gets the score value associated with the score action
     * @return the score value associated with the score action
     */
    public int getPoints() {
        return points;
    }
}
