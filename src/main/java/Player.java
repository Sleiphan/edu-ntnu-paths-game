import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the player character of the game
 */
public class Player {
    private final String name;
    private int health;
    private int score;
    private int gold;
    private final List<String> inventory;

    /**
     * Initializes a player object. Throws exceptions if health is less than 1, or if score and gold is less than 0
     * @param name      The name of the player character
     * @param health    The baseline health of the player character
     * @param score     The baseline score of the player character
     * @param gold      The baseline gold of the player character
     */
    public Player(String name, int health, int score, int gold){
        this.name = name;

        if (health > 0){
            this.health = health;
        } else {
            throw new RuntimeException("Health can't be less than 1");
        }

        if (score >= 0){
            this.score = score;
        } else {
            throw new RuntimeException("Score can't be less than 0");
        }

        if (gold >= 0){
            this.gold = gold;
        } else {
            throw new RuntimeException("Gold can't be less than 0");
        }

        inventory = new ArrayList<>();
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
     * Returns the player-objects inventory array
     * @return the player-objects inventory array
     */
    public List<String> getInventory() {
        return inventory;
    }
}
