package edu.ntnu.idatt2001.paths.goal;

import edu.ntnu.idatt2001.paths.Player;

public interface Goal {

    //String type = null;
    Object value = null;

    public boolean isFulfilled(Player player);

}
