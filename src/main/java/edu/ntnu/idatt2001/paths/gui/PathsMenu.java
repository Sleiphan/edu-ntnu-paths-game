package edu.ntnu.idatt2001.paths.gui;

import javafx.scene.Scene;

public abstract class PathsMenu implements ApplicationState {

    protected PathsMenuSystem handler;

    @Override
    public void setHandlerRef(ApplicationStateHandler handlerRef) {
        if (!(handlerRef instanceof PathsMenuSystem menuSystem))
            throw new IllegalArgumentException("This menu is only compatible with a handler of type PathsMenuSystem");

        this.handler = menuSystem;
    }

    /**
     * Call this method to switch to another menu. This method calls the current
     * application state's cleanup-method, changes the current state to the submitted
     * new state, and calls the setup-method of the new state. <br>
     * <br>
     * In canon, this method would be called when the user presses a button on a
     * menu that opens a submenu. To perform the changeover to the requested submenu,
     * the button would call this method to de-initialise its own menu and start up
     * the new submenu. <br>
     * <br>
     * This kind of structure requires the button object to
     * take into account that its associated menu will perform de-initialisation,
     * and possibly de-reference the button-object during this method call.<br>
     *
     * @param newState The new application state to be the new current state.
     */
    public final void changeState(ApplicationState newState) {
        if (!(newState instanceof PathsMenu fxState))
            throw new IllegalArgumentException("This menu is only compatible with a handler of type PathsMenuSystem");

        this.handler.changeState(fxState);
    }

    public abstract Scene getScene();
}
