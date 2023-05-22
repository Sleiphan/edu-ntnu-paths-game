package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.model.Game;
import edu.ntnu.idatt2001.paths.model.Player;
import edu.ntnu.idatt2001.paths.gui.gameplayer.GameScene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class InGameMenu extends PathsMenu {

    private final GameScene associatedGameScene;

    public InGameMenu(GameScene currentGameScene) {
        this.associatedGameScene = currentGameScene;
    }

    /**
     * Calling this method allows the user to restart the game, taking them back
     * to the opening passage whilst also resetting the player parameters back
     * to their original state. Asks the user for confirmation before resetting.
     * @param e
     */
    private void restartGame(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Reset");
        alert.setHeaderText("Reset game");
        alert.setContentText("Are you sure you want to reset the game?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            Player player = new Player.PlayerBuilder(handler.getInitialPlayer().getName(),
                    handler.getInitialPlayer().getHealth()).setGold(handler.getInitialPlayer().getGold())
                    .build();
            Game initialGame = new Game(player, handler.getStoryLoader().getStory(),handler.getInitialGoals());
            changeState(new GameScene(initialGame,handler.getStoryLoader(),handler.getSceneConfig()));
        }

    }

    /**
     * Calling this method returns the user back to the main menu of the application.
     * All progress in current story will be lost.
     * @param e
     */
    private void mainMenu(ActionEvent e){
        changeState(new MainMenu());
    }

    /**
     * Calling this method allows the user to quit the application
     * All progress in current story will be lost
     * @param e
     */
    private void quitGame(ActionEvent e){
        Platform.exit();
    }

    /**
     * Calling this method closes the in game menu and returns the user to the current gameScene passage
     * @param e
     */
    private void closeMenu(ActionEvent e) {
        changeState(associatedGameScene);
    }

    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }

    /**
     * Calling this method initializes the in game menu scene.
     * @return the in game menu scene
     */
    @Override
    public Scene getScene() {

        Button btRestart = new Button("Restart");
        Button btMainMenu = new Button("Main menu");
        Button btQuit = new Button("Quit");
        Button btClose = new Button("Close");

        btRestart.setId("Restart");
        btMainMenu.setId("MainMenu");
        btQuit.setId("Quit");
        btClose.setId("Close");

        btRestart.setOnAction(this::restartGame);
        btMainMenu.setOnAction(this::mainMenu);
        btQuit.setOnAction(this::quitGame);
        btClose.setOnAction(this::closeMenu);

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();

        btRestart.setPrefWidth(200);
        btMainMenu.setPrefWidth(200);
        btQuit.setPrefWidth(200);
        btClose.setPrefWidth(200);

        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        btRestart.setTranslateX(buttonX);
        btMainMenu.setTranslateX(buttonX);
        btQuit.setTranslateX(buttonX);
        btClose.setTranslateX(buttonX);

        btRestart.setTranslateY(170);
        btMainMenu.setTranslateY(270);
        btQuit.setTranslateY(370);
        btClose.setTranslateY(470);

        AnchorPane root = new AnchorPane();

        root.getChildren().add(btRestart);
        root.getChildren().add(btMainMenu);
        root.getChildren().add(btQuit);
        root.getChildren().add(btClose);

        Scene scene = new Scene(root, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());
        scene.getStylesheets().add("Button.css");
        scene.getStylesheets().add("InGameMenu.css");

        return scene;
    }
}
