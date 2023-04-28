package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.gui.appstate.ApplicationState;
import javafx.scene.Scene;

public class GameScene extends PathsMenu {

    private Story gameStory;

    public GameScene(Story s) {
        this.gameStory = s;
    }



    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public Scene getScene() {
        return null;
    }
}
