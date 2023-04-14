package edu.ntnu.idatt2001.paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A class representing the player character of the game
 */
public class Player {
    // Mandatory parameters
    private final String name;
    private int health;

    // Optional parameters
    private int score;
    private int gold;
    private final List<String> inventory;

    /**
     * A private constructor used in the builder to create an instance of the object
     * @param builder   Builder class used to make the player Object
     */
    private Player(PlayerBuilder builder){
        this.name = builder.name;
        this.health = builder.health;
        this.score = builder.score;
        this.gold = builder.gold;
        this.inventory = builder.inventory;
    }

    /**
     * A builder class used to make Player objects
     */
    public static class PlayerBuilder{
        // Mandatory parameters
        private final String name;
        private final int health;

        // Optional parameters
        private int score;
        private int gold;
        private List<String> inventory;

        /**
         * Initializes the mandatory parameters of the player object
         * @param name      name of the player
         * @param health    baseline health of the player
         */
        public PlayerBuilder(String name, int health){
            this.name = name;

            if (health > 0){
                this.health = health;
            } else {
                throw new IllegalArgumentException("Health can't be less than 1");
            }
        }

        /**
         * Initializes the optional score parameter of the player object
         * @param score     baseline score
         * @return          returns this builder
         */
        public PlayerBuilder setScore(int score){
            if (score >= 0){
                this.score = score;
                return this;
            } else {
                throw new IllegalArgumentException("Score can't be less than 0");
            }
        }

        /**
         * Initializes the optional gold parameter of the player object
         * @param gold      baseline score
         * @return          returns this builder
         */
        public PlayerBuilder setGold(int gold){
            if (gold >= 0){
                this.gold = gold;
                return this;
            } else {
                throw new IllegalArgumentException("Gold can't be less than 0");
            }
        }

        /**
         * Initializes the optional inventory parameter of the player object
         * @param inventory     baseline inventory
         * @return              returns this builder
         */
        public PlayerBuilder setInventory(List<String> inventory){
            this.inventory = inventory;
            return this;
        }

        /**
         * Creates the player object
         * @return the new player object
         */
        public Player build(){
            return new Player(this);
        }
    }

    /**
     * Returns the name of the player-object
     * @return the name of the player-object
     */
    public String getName() {
        return name;
    }

    /**
     * Increases the health of the player-object
     * @param healthToAdd   int value to increase health by
     * @return              True if health increase successful, false if not
     */
    public boolean addHealth(int healthToAdd){
        if(healthToAdd > 0){
            health += healthToAdd;
            return true;
        }
        return false;
    }

    /**
     * Decreases the health of the player-object
     * @param healthToRemove    int value to decrease health by
     * @return                  True if health decrease successful, false if not
     */
    public boolean reduceHealth(int healthToRemove){
        if(healthToRemove < 0){
            health -= healthToRemove;
            return true;
        }
        return false;
    }

    /**
     * Returns the health of the player-object
     * @return the health of the player-object
     */
    public int getHealth() {
        return health;
    }

    /**
     * Increases the score of the player-object
     * @param scoreToAdd    int value to increase score by
     * @return              True if score increase successful, false if not
     */
    public boolean addScore(int scoreToAdd){
        if(scoreToAdd > 0){
            score += scoreToAdd;
            return true;
        }
        return false;
    }

    /**
     * Decreases the score of the player-object
     * @param scoreToRemove int value to decrease score by
     * @return              True if decrease successful, false if not
     */
    public boolean reduceScore(int scoreToRemove){
        if(scoreToRemove < 0){
            score -= scoreToRemove;
            return true;
        }
        return false;
    }

    /**
     * Returns the score of the player-object
     * @return the score of the player-object
     */
    public int getScore() {
        return score;
    }

    /**
     * Increases the gold of the player-object
     * @param goldToAdd int value to increase by
     * @return          True if increase successful, false if not
     */
    public boolean addGold(int goldToAdd){
        if(goldToAdd > 0){
            gold += goldToAdd;
            return true;
        }
        return false;
    }

    /**
     * Reduces the gold of the player-object
     * @param goldToRemove  int value to decrease gold by
     * @return              True if decrease successful, false if not
     */
    public boolean reduceGold(int goldToRemove){
        if(goldToRemove < 0){
            gold -= goldToRemove;
            return true;
        }
        return false;
    }

    /**
     * Returns the gold of the player-object
     * @return the gold of the player-object
     */
    public int getGold() {
        return gold;
    }

    /**
     * Adds an item to the player-objects inventory array
     * @param item  item to add to inventory
     * @return      true if addition successful
     */
    public boolean addToInventory(String item){
        inventory.add(item);
        return true;
    }

    /**
     * Removes an item from the player-objects inventory array
     * @param item  item to remove from inventory
     * @return      true if removal successful, false if not
     */
    public boolean removeFromInventory(String item){
        if(inventory.contains(item)){
            inventory.remove(item);
            return true;
        }
        return false;
    }

    /**
     * Returns the player-objects inventory array
     * @return the player-objects inventory array
     */
    public List<String> getInventory() {
        return inventory;
    }
}
