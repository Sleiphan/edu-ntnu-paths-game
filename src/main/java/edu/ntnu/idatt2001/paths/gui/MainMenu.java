package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.asset.AudioAsset;
import edu.ntnu.idatt2001.paths.gui.gameplayer.AssetFinder;
import edu.ntnu.idatt2001.paths.io.PathsParser;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;


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

    private void generateAssetTemplate(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("Stories"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Paths Files", "*.paths")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        File targetFile = new File(selectedFile.getAbsolutePath() + "assets");
        boolean exists = Files.exists(Path.of(targetFile.getAbsolutePath()));
        if(!exists){
            AssetFinder.generateAssetTemplate(selectedFile);
            return;
        }

        boolean delete = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Asset file already exists");
        alert.setHeaderText("Asset file already exists");
        alert.setContentText("The asset file for your chosen file already exists." +
                " Generating a asset template will delete the old asset template for the chosen file." +
                " Proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            delete = true;
        }

        if(delete){
            try {
                Files.delete(Path.of(targetFile.getAbsolutePath()));
            } catch (Exception ex){
                throw new IllegalArgumentException("File doesn't exist");
            }

            AssetFinder.generateAssetTemplate(selectedFile);
        }

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

        Button btNewGame                = new Button("New Game");
        Button btTutorial               = new Button("Tutorial");
        Button btQuit                   = new Button("Quit");
        Button btGenerateAssetTemplate  = new Button("Make Asset\nTemplate");
        ImageView logo = new ImageView();
        logo.setId("Logo");

        Font.loadFont("file:src/main/resources/alagard/alagard.ttf",20);

        btTutorial             .setOnAction(this::Tutorial);
        btNewGame              .setOnAction(this::newGame);
        btQuit                 .setOnAction(this::quit);
        btGenerateAssetTemplate.setOnAction(this::generateAssetTemplate);
        btGenerateAssetTemplate.setId("AssetTemplate");

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();

        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        btTutorial.setTranslateX(buttonX);
        btNewGame .setTranslateX(buttonX);
        btQuit    .setTranslateX(buttonX);
        btGenerateAssetTemplate.setTranslateX(buttonX);
        logo.setTranslateX(buttonX);

        btTutorial.setPrefWidth(buttonWidth);
        btNewGame .setPrefWidth(buttonWidth);
        btQuit    .setPrefWidth(buttonWidth);
        btGenerateAssetTemplate.setPrefWidth(buttonWidth);

        btTutorial.setTranslateY(350);
        btNewGame .setTranslateY(270);
        btQuit    .setTranslateY(550);
        btGenerateAssetTemplate.setTranslateY(430);
        logo.setTranslateY(40);

        logo.setFitHeight(200);
        logo.setFitWidth(200);



        root.getChildren().add(btTutorial);
        root.getChildren().add(btNewGame);
        root.getChildren().add(btQuit);
        root.getChildren().add(btGenerateAssetTemplate);
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
