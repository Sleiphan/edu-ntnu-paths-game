package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.io.StoryLoader;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Player;
import edu.ntnu.idatt2001.paths.model.goal.Goal;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathsMenuSystem implements ApplicationStateHandler {

    private final Stage applicationWindow;
    StoryLoader storyLoader;
    Player initialPlayer;
    List<Goal> initialGoals = new ArrayList<>();
    SceneConfig sceneConfig = new SceneConfig(1270, 720);
    MediaPlayer menuMusic;
    private PathsMenu currentState;
    private String currentAudio = "";
    private String currentFileName = "";
    private String currentPath = "";
    private List<Link> currentBrokenLinks = new ArrayList<>();

    /**
     * Creates a new instance of FXHandler running an initial FXAppState.
     *
     * @param initialState      The first application state that this handler will be running.
     * @param applicationWindow A reference to the JavaFX application window.
     */
    public PathsMenuSystem(PathsMenu initialState, Stage applicationWindow) {
        this.applicationWindow = applicationWindow;
        applicationWindow.setResizable(false);
        changeState(initialState);
    }

    public Color getBgColor() {
        return Color.color(1, 1, 1);
    }

    public int getSceneWidth() {
        return 1270;
    }

    public int getSceneHeight() {
        return 720;
    }

    @Override
    public void changeState(ApplicationState newState) {
        if (newState == null)
            throw new IllegalArgumentException("newState cannot be null");

        if (!(newState instanceof PathsMenu newMenu))
            throw new IllegalArgumentException("This ApplicationStateHandler can only handle ApplicationStates of type PathsMenu");

        if (currentState != null)
            currentState.cleanup();

        currentState = newMenu;

        currentState.setHandlerRef(this);
        currentState.setup();
        applicationWindow.setScene(newMenu.getScene());
    }

    public String getCurrentAudio() {
        return currentAudio;
    }

    public void setCurrentAudio(String currentAudio) {
        this.currentAudio = currentAudio;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    /**
     * Formats and shortens the path of the file currently in user by the system.
     *
     * @param currentPath path to format
     */
    public void setCurrentPath(String currentPath) {
        if (currentPath.length() > 50) {
            String split = "\\\\";
            if (currentPath.contains("/")) {
                split = "/";
            }
            String[] splitPath = currentPath.split(split);
            String shortenedPath = splitPath[0] + "/" + splitPath[1] + "/" + splitPath[2] + "/.../"
                    + splitPath[splitPath.length - 2] + "/" + splitPath[splitPath.length - 1];
            this.currentPath = shortenedPath;

        } else {
            this.currentPath = currentPath;
        }
    }

    public List<Link> getCurrentBrokenLinks() {
        return currentBrokenLinks;
    }

    public void setCurrentBrokenLinks(List<Link> currentBrokenLinks) {
        this.currentBrokenLinks = currentBrokenLinks;
    }

    public Player getInitialPlayer() {
        return initialPlayer;
    }

    public void setInitialPlayer(Player initialPlayer) {
        this.initialPlayer = initialPlayer;
    }

    public List<Goal> getInitialGoals() {
        return initialGoals;
    }

    public void setInitialGoals(List<Goal> initialGoals) {
        this.initialGoals = initialGoals;
    }

    public SceneConfig getSceneConfig() {
        return sceneConfig;
    }

    public StoryLoader getStoryLoader() {
        return storyLoader;
    }

    public void setStoryLoader(StoryLoader storyLoader) {
        this.storyLoader = storyLoader;
    }

    /**
     * Plays the MainMenu Music on loop.
     */
    public void playMenuMusic() {
        String musicFile = "src/main/resources/Img/MainMenu.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        menuMusic = new MediaPlayer(sound);
        menuMusic.setVolume(0.05);
        menuMusic.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                menuMusic.seek(Duration.ZERO);
            }
        });
        menuMusic.play();
    }

    /**
     * Stops the menu music.
     */
    public void stopMenuMusic() {
        menuMusic.stop();
    }
}
