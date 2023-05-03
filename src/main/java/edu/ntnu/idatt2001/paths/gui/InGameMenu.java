package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.gui.gameplayer.GameScene;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class InGameMenu extends PathsMenu {

    private final GameScene associatedGameScene;

    public InGameMenu(GameScene currentGameScene) {
        this.associatedGameScene = currentGameScene;
    }

    private void saveGame(ActionEvent e){}

    private void quitGame(ActionEvent e){}

    private void closeMenu(ActionEvent e) {
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

        Button btSave = new Button("Save");
        Button btQuit = new Button("Quit");
        Button btClose = new Button("Close");


        btSave.setOnAction(this::saveGame);
        btQuit.setOnAction(this::quitGame);
        btClose.setOnAction(this::closeMenu);

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();

        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        btSave.setTranslateX(buttonX);
        btQuit.setTranslateX(buttonX);
        btClose.setTranslateX(buttonX);

        btSave.setTranslateY(270);
        btQuit.setTranslateY(370);
        btClose.setTranslateY(470);

        AnchorPane root = new AnchorPane();

        root.getChildren().add(btSave);
        root.getChildren().add(btQuit);
        root.getChildren().add(btClose);

        Scene scene = new Scene(root, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());

        return scene;
    }
}
