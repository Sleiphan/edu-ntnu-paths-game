package edu.ntnu.idatt2001.paths;

import edu.ntnu.idatt2001.paths.gui.PathsMenuSystem;
import edu.ntnu.idatt2001.paths.gui.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class PathsApplication extends Application implements Runnable {

    private Stage stage;
    private PathsMenuSystem handler;

    public PathsApplication() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        handler = new PathsMenuSystem(new MainMenu(), stage);
        stage.show();
    }

    @Override
    public void run() {
        launch();
    }
}
