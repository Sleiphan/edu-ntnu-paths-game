package edu.ntnu.idatt2001.paths.gui;

import javafx.scene.Scene;

public abstract class PathsMenu implements ApplicationState<PathsMenuSystem> {

    protected PathsMenuSystem handler;

    @Override
    public void setHandlerRef(PathsMenuSystem handlerRef) {
        this.handler = handlerRef;
    }

    @Override
    public final void changeState(ApplicationState<PathsMenuSystem> newState) {
        if (!(newState instanceof PathsMenu fxState))
            throw new IllegalArgumentException("");

        this.handler.changeState(fxState);
    }

    public abstract Scene getScene();
}
