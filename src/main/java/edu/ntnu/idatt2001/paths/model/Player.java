package edu.ntnu.idatt2001.paths.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the player character of the game.
 */
public class Player {
    // Mandatory parameters
    private final String name;
    private final List<String> inventory;
    private int health;
    // Optional parameters
    private int score;
    private int gold;

    /**
     * A private constructor used in the builder to create
     * an instance of the object.
     *
     * @param builder Builder class used to make the player Object
     */
    private Player(PlayerBuilder builder) {
        this.name = builder.name;
        this.health = builder.health;
        this.score = builder.score;
        this.gold = builder.gold;
        this.inventory = builder.inventory;
    }

    /**
     * Returns the name of the player-object.
     *
     * @return the name of the player-object
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the health of the player.
     *
     * @param healthChange int value denoting the amount of change.
     */
    public void changeHealth(int healthChange) {
        health += healthChange;
    }

    /**
     * Changes the player's score.
     *
     * @param scoreChange int value denoting the amount of change.
     */
    public void changeScore(int scoreChange) {
        score += scoreChange;
    }

    /**
     * Changes the player's amount of owned gold.
     *
     * @param goldChange int value denoting the amount of change.
     */
    public void changeGold(int goldChange) {
        gold += goldChange;
    }

    /**
     * Returns the health of the player-object.
     *
     * @return the health of the player-object
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the score of the player-object.
     *
     * @return the score of the player-object
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the gold of the player-object.
     *
     * @return the gold of the player-object
     */
    public int getGold() {
        return gold;
    }

    /**
     * Adds an item to the player-objects inventory.
     *
     * @param item The item to add to inventory.
     */
    public void addToInventory(String item) {
        inventory.add(item);
    }

    /**
     * Removes an item from the player-objects inventory.
     *
     * @param item The item to remove from inventory.
     * @return True if an item was found and removed. Otherwise, returns false.
     */
    public boolean removeFromInventory(String item) {
        return inventory.remove(item);
    }

    /**
     * Checks the inventory of this player, and indicates whether an
     * item with the submitted name is present or not.
     * This method does not take into account the amount of items available.
     * If there is 1 or more item
     * with the specified name present, this method will
     * return <code>true</code>.
     *
     * @param item The item to find.
     * @return <code>true</code> if an item with the submitted name is present.
     * Otherwise, returns <code>false</code>.
     */
    public boolean hasItem(String item) {
        return inventory.stream().anyMatch(item::equals);
    }

    /**
     * Returns the player-objects inventory array.
     *
     * @return the player-objects inventory array
     */
    public List<String> getInventory() {
        return inventory;
    }

    /**
     * A builder class used to make Player objects.
     */
    public static class PlayerBuilder {
        // Mandatory parameters
        private final String name;
        private final int health;

        // Optional parameters
        private int score;
        private int gold;
        private List<String> inventory;

        /**
         * Initializes the mandatory parameters of the player object.
         *
         * @param name   name of the player
         * @param health baseline health of the player
         */
        public PlayerBuilder(String name, int health) {
            this.name = name;

            if (health > 0) {
                this.health = health;
            } else {
                throw new IllegalArgumentException("Health can't be less than 1");
            }
        }

        /**
         * Initializes the optional score parameter of the player object.
         *
         * @param score baseline score
         * @return returns this builder
         */
        public PlayerBuilder setScore(int score) {
            if (score >= 0) {
                this.score = score;
                return this;
            } else {
                throw new IllegalArgumentException("Score can't be less than 0");
            }
        }

        /**
         * Initializes the optional gold parameter of the player object.
         *
         * @param gold baseline score
         * @return returns this builder
         */
        public PlayerBuilder setGold(int gold) {
            if (gold >= 0) {
                this.gold = gold;
                return this;
            } else {
                throw new IllegalArgumentException("Gold can't be less than 0");
            }
        }

        /**
         * Initializes the optional inventory parameter of the player object.
         *
         * @param inventory baseline inventory
         * @return returns this builder
         */
        public PlayerBuilder setInventory(List<String> inventory) {
            this.inventory = inventory;
            return this;
        }

        /**
         * Creates the player object.
         *
         * @return the new player object
         */
        public Player build() {
            if (inventory == null)
                inventory = new ArrayList<>();
            return new Player(this);
        }
    }
}
