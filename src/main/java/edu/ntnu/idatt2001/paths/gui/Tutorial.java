package edu.ntnu.idatt2001.paths.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Tutorial extends PathsMenu {

    AnchorPane anchorPane = new AnchorPane();

    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }

    /**
     * Creates the tutorial scene.
     * Iterates through the different tutorial images as the user clicks anywhere in the scene.
     * When the user reaches the final image in the tutorial series they will be
     * returned to the main menu.
     *
     * @return the tutorial scene.
     */
    @Override
    public Scene getScene() {
        Button btBack = new Button("Back");
        btBack.setOnAction(this::back);
        btBack.setTranslateX(1170);
        ImageView tut = new ImageView();
        tut.setImage(new Image("Tutorial/Tutorial_1.png"));

        anchorPane.getChildren().add(tut);
        anchorPane.getChildren().add(btBack);

        Scene scene = new Scene(anchorPane, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());
        scene.getStylesheets().add("Button.css");
        scene.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                String currentTut = tut.getImage().getUrl();
                String tutNumb = currentTut.substring(currentTut.indexOf("_") + 1, currentTut.indexOf("."));
                if (tutNumb.equals("11")) {
                    back(new ActionEvent());
                } else {

                    int newTutNumb = Integer.parseInt(tutNumb) + 1;

                    tut.setImage(new Image("Tutorial/Tutorial_" + newTutNumb + ".png"));
                }


            }
        });

        return scene;
    }

    /**
     * Takes the user back to the mainMenu.
     */
    private void back(ActionEvent e) {
        changeState(new MainMenu());
    }
}
