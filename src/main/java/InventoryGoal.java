import java.util.List;
import java.util.Objects;

public class InventoryGoal implements Goal{
    private final List<String> mandatoryItems;

    public InventoryGoal(List<String> mandatoryItems){
        this.mandatoryItems = mandatoryItems;
    }

    @Override
    public boolean isFulfilled(Player player) {
        if (mandatoryItems.size() > player.getInventory().size()){
            return false;
        } else {
            for(int i = 0; i < mandatoryItems.size(); i++){
                for (int j = 0; j < player.getInventory().size(); j++){
                    if (Objects.equals(mandatoryItems.get(i), player.getInventory().get(j))){
                        i++;
                        j = 0;
                    } else if (!Objects.equals(mandatoryItems.get(i), player.getInventory().get(j)) && i == mandatoryItems.size()){
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
