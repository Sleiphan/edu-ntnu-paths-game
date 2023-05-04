package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.gui.gameplayer.GameScene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class InGameMenu extends PathsMenu {

    private final GameScene associatedGameScene;

    public InGameMenu(GameScene currentGameScene) {
        this.associatedGameScene = currentGameScene;
    }

    private void saveGame(ActionEvent e){}

    private void options(ActionEvent e){}

    private void mainMenu(ActionEvent e){
        changeState(new MainMenu());
    }

    private void quitGame(ActionEvent e){
        Platform.exit();
    }

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

        ImageView overlay = new ImageView();
        overlay.setId("overlay");

        overlay.setFitWidth(10000);
        overlay.setFitWidth(10000);

        Button btSave = new Button("Save");
        Button btOptions = new Button("Options");
        Button btMainMenu = new Button("Main menu");
        Button btQuit = new Button("Quit");
        Button btClose = new Button("Close");

        btSave.setId("Save");
        btOptions.setId("Options");
        btMainMenu.setId("MainMenu");
        btQuit.setId("Quit");
        btClose.setId("Close");

        btSave.setOnAction(this::saveGame);
        btOptions.setOnAction(this::options);
        btMainMenu.setOnAction(this::mainMenu);
        btQuit.setOnAction(this::quitGame);
        btClose.setOnAction(this::closeMenu);

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();

        btSave.setPrefWidth(200);
        btOptions.setPrefWidth(200);
        btMainMenu.setPrefWidth(200);
        btQuit.setPrefWidth(200);
        btClose.setPrefWidth(200);

        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        btSave.setTranslateX(buttonX);
        btOptions.setTranslateX(buttonX);
        btMainMenu.setTranslateX(buttonX);
        btQuit.setTranslateX(buttonX);
        btClose.setTranslateX(buttonX);

        btSave.setTranslateY(170);
        btOptions.setTranslateY(270);
        btMainMenu.setTranslateY(370);
        btQuit.setTranslateY(470);
        btClose.setTranslateY(570);

        AnchorPane root = new AnchorPane();

        //root.getChildren().add(overlay);
        root.getChildren().add(btSave);
        root.getChildren().add(btOptions);
        root.getChildren().add(btMainMenu);
        root.getChildren().add(btQuit);
        root.getChildren().add(btClose);

        Scene scene = new Scene(root, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());
        scene.getStylesheets().add("Button.css");
        scene.getStylesheets().add("InGameMenu.css");

        return scene;
    }
}
