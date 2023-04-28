package edu.ntnu.idatt2001.paths.gui;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class NewGameMenu extends PathsMenu {



    public NewGameMenu() {

    }

    private void back(ActionEvent e) {
        changeState(new MainMenu());
    }

    @Override
    public Scene getScene() {
        Button btBack = new Button("Back");
        btBack.setTranslateX(20);
        btBack.setTranslateY(20);
        btBack.setPrefWidth(70);
        btBack.setOnAction(this::back);

        Group root = new Group(
                btBack
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
