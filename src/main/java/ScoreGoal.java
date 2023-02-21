public class ScoreGoal implements Goal {
    private final int minimumPoints;

    public ScoreGoal(int minimumPoints){
        this.minimumPoints = minimumPoints;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getScore() >= minimumPoints;
    }
}
