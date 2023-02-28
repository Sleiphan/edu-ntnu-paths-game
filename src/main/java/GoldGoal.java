/**
 * Used to check if a player-object fulfills a minimum gold requirement
 */
public class GoldGoal implements Goal{
    private final int minimumGold;

    /**
     * Assigns a minimum gold value
     * @param minimumGold minimum allowed gold value
     */
    public GoldGoal(int minimumGold){
        this.minimumGold = minimumGold;
    }

    /**
     * Checks if a player-object fulfills a minimum gold requirement
     * @param player    player-object to check if fulfills the minimum requirement
     * @return          True if requirement met, false if not
     */
    @Override
    public boolean isFulfilled(Player player) {
        return player.getGold() >= minimumGold;
    }
}
