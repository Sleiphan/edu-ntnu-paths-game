package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.Link;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PathsMenuSystem implements ApplicationStateHandler<PathsMenu> {

    private PathsMenu currentState;
    private final Stage applicationWindow;

    private String currentAudio = "";

    private String currentFileName = "";

    private String currentPath = "";

    private List<Link> currentBrokenLinks = new ArrayList<>();

    /**
     * Creates a new instance of FXHandler running an initial FXAppState.
     *
     * @param initialState The first application state that this handler will be running.
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

    public void setCurrentFileName(String currentFileName){this.currentFileName = currentFileName;}

    public String getCurrentFileName(){return currentFileName;}

    public void setCurrentPath(String currentPath){
        if(currentPath.length() > 50){
            String split = "\\\\";
            if(currentPath.contains("/")){
                split = "/";
            }
            String[] splitPath= currentPath.split(split);
            String shortenedPath = splitPath[0] + "/" + splitPath[1] + "/" +splitPath[2] + "/.../" +
                    splitPath[splitPath.length - 2] + "/" + splitPath[splitPath.length - 1];
            this.currentPath = shortenedPath;

        }
        else{
            this.currentPath = currentPath;
        }
    }

    public String getCurrentPath(){return currentPath;}

    public void setCurrentBrokenLinks(List<Link> currentBrokenLinks) {
        this.currentBrokenLinks = currentBrokenLinks;
    }

    public List<Link> getCurrentBrokenLinks() {
        return currentBrokenLinks;
    }
}
