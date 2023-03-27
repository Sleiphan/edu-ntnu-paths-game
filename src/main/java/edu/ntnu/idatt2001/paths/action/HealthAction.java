package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;

/**
 * A class for executing a change in a player objects health field
 */
public class HealthAction implements Action{
    private final int health;

    /**
     * Initializes the value to change a player objects health field by
     * @param health amount to change a player objects health field by
     */
    public HealthAction(int health){
        this.health = health;
    }

    /**
     * Executes the change to the player object given as a parameter's health field
     * @param player    player to change health of
     * @return          True if health changed, false if not
     */
    @Override
    public boolean execute(Player player) {
        Player p = new Player("Test name", 1,1,1);
        if(health > 0){
            p.addHealth(health);
            return true;
        } else if (health < 0) {
            p.reduceHealth(health);
            return true;
        }
        return false;
    }

    /**
     * Converts the HealthAction to string that can be written to the .paths format
     * @return the HealthAction as a string
     */
    public String toPathsFormat() {
        return "{healthAction:" + health +"}";
    }

    /**
     * Reads HealthAction from a string
     * @param pathsString   The string to search for health action
     * @return              Null if no health action found, a new health action if health action found
     */
    public static HealthAction fromPathsFormat(String pathsString) {
        boolean checkIfHealthAction = pathsString.contains("{healthAction:");
        if(checkIfHealthAction){
            StringBuilder current = new StringBuilder();
            StringBuilder health = new StringBuilder();
            for(int i = 0; i < pathsString.length(); i++){
                if(current.toString().contains("{healthAction:")){
                    if (pathsString.charAt(i) == '}') {
                        return new HealthAction(Integer.parseInt(health.toString()));
                    }
                    health.append(pathsString.charAt(i));
                }
                current.append(pathsString.charAt(i));
            }
        }
        return null;
    }

    /**
     * Gets the health value associated with the health action
     * @return the health value associated with the health action
     */
    public int getHealth() {
        return health;
    }
}
