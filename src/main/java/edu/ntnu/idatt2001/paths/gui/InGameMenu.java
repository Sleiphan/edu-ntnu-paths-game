package edu.ntnu.idatt2001.paths.gui;

import javafx.scene.Scene;

public class InGameMenu extends PathsMenu {

    private final GameScene associatedGameScene;

    public InGameMenu(GameScene currentGameScene) {
        this.associatedGameScene = currentGameScene;
    }



    private void closeMenu() {
        changeState(associatedGameScene);
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
