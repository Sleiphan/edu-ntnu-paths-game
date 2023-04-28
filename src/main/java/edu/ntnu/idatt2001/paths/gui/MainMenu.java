package edu.ntnu.idatt2001.paths.gui;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class MainMenu extends PathsMenu {

    public MainMenu() {

    }

    private void continueLastGame(ActionEvent e) {

    }

    private void newGame(ActionEvent e) {
        changeState(new NewGameMenu());
    }

    private void quit(ActionEvent e) {

    }

    @Override
    public Scene getScene() {
        Button btContinue = new Button("Continue");
        Button btNewGame  = new Button("New Game");
        Button btQuit     = new Button("Quit");

        btContinue.setOnAction(this::continueLastGame);
        btNewGame .setOnAction(this::newGame);
        btQuit    .setOnAction(this::quit);

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();

        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        btContinue.setTranslateX(buttonX);
        btNewGame .setTranslateX(buttonX);
        btQuit    .setTranslateX(buttonX);
        btContinue.setPrefWidth(buttonWidth);
        btNewGame .setPrefWidth(buttonWidth);
        btQuit    .setPrefWidth(buttonWidth);

        btContinue.setTranslateY( 50);
        btNewGame .setTranslateY(100);
        btQuit    .setTranslateY(150);

        Group root = new Group(
                btContinue,
                btNewGame,
                btQuit
        );
        root.getStylesheets().add("StyleSheet.css");

        return new Scene(root, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());
    }



    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }
}
