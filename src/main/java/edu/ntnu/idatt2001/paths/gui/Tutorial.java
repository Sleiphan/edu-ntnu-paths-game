package edu.ntnu.idatt2001.paths.gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.event.MouseEvent;

public class Tutorial extends PathsMenu{

    AnchorPane anchorPane = new AnchorPane();

    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public Scene getScene() {
        Button btBack = new Button("Back");
        ImageView tut = new ImageView();
        tut.setImage(new Image("Tutorial/Tutorial_1.png"));

        anchorPane.getChildren().add(tut);
        anchorPane.getChildren().add(btBack);

        Scene scene = new Scene(anchorPane,handler.getSceneWidth(),handler.getSceneHeight(),handler.getBgColor());
        scene.getStylesheets().add("Button.css");
        scene.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                String currentTut = tut.getImage().getUrl();
                String tutNumb = currentTut.substring(currentTut.indexOf("_") + 1, currentTut.indexOf("."));
                if (tutNumb.equals("11")){
                    back();
                } else {

                    int newTutNumb = Integer.parseInt(tutNumb) + 1;

                    tut.setImage(new Image("Tutorial/Tutorial_"+newTutNumb+".png"));
                }


            }
        });

        return scene;
    }

    private void back(){
        changeState(new MainMenu());
    }
}
