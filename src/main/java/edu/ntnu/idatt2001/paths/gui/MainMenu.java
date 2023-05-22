package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.asset.AudioAsset;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;




public class MainMenu extends PathsMenu {


    AnchorPane root = new AnchorPane();

    public MainMenu() {

    }

    private void Tutorial(ActionEvent e) {
        changeState(new Tutorial());
    }

    /**
     * Takes the user to the new game menu scene, where the user can initialize a new game
     * @param e
     */
    private void newGame(ActionEvent e){
        changeState(new NewGameMenu());
    }

    /**
     * Allows the user to quit the application
     * @param e
     */
    private void quit(ActionEvent e) {
        Platform.exit();
    }

    /**
     * Initializes the main menu scene. Sets the current audio the main menu music unless the music is already
     * the main menu music.
     * Loads the font that gets used throughout the entirety of the user interface.
     * Uses the mainMenu css stylesheet to style the page.
     * @return the main menu scene
     */
    @Override
    public Scene getScene() {

        if(!handler.getCurrentAudio().equals("MainMenu")){
            handler.setCurrentAudio("MainMenu");
            handler.playMenuMusic();
        }

        Button btContinue = new Button("Tutorial");
        Button btNewGame  = new Button("New Game");
        Button btQuit     = new Button("Quit");
        ImageView logo = new ImageView();
        logo.setId("Test");

        Font.loadFont("file:src/main/resources/alagard/alagard.ttf",20);

        btContinue.setOnAction(this::Tutorial);
        btNewGame .setOnAction(this::newGame);
        btQuit    .setOnAction(this::quit);

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();

        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        btContinue.setTranslateX(buttonX);
        btNewGame .setTranslateX(buttonX);
        btQuit    .setTranslateX(buttonX);
        logo.setTranslateX(buttonX);

        btContinue.setPrefWidth(buttonWidth);
        btNewGame .setPrefWidth(buttonWidth);
        btQuit    .setPrefWidth(buttonWidth);

        btContinue.setTranslateY(370);
        btNewGame .setTranslateY(270);
        btQuit    .setTranslateY(470);
        logo.setTranslateY(40);

        logo.setFitHeight(200);
        logo.setFitWidth(200);



        root.getChildren().add(btContinue);
        root.getChildren().add(btNewGame);
        root.getChildren().add(btQuit);
        root.getChildren().add(logo);



        Scene scene = new Scene(root, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());
        scene.getStylesheets().add("MainMenu.css");
        return scene;
    }



    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }
}
