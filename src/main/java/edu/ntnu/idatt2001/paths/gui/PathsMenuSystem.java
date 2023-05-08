package edu.ntnu.idatt2001.paths.gui;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PathsMenuSystem implements ApplicationStateHandler<PathsMenu> {

    private PathsMenu currentState;
    private final Stage applicationWindow;

    private String currentAudio = "";

    /**
     * Creates a new instance of FXHandler running an initial FXAppState.
     *
     * @param initialState The first application state that this handler will be running.
     * @param applicationWindow A reference to the JavaFX application window.
     */
    public PathsMenuSystem(PathsMenu initialState, Stage applicationWindow) {
        this.applicationWindow = applicationWindow;
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
    public void changeState(PathsMenu newState) {
        if (currentState != null)
            currentState.cleanup();

        assert newState != null;
        currentState = newState;

        currentState.setHandlerRef(this);
        currentState.setup();
        applicationWindow.setScene(newState.getScene());
    }

    public void setCurrentAudio(String currentAudio){
        this.currentAudio = currentAudio;
    }

    public String getCurrentAudio(){
        return currentAudio;
    }
}
