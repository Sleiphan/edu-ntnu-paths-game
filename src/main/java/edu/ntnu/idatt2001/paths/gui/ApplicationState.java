package edu.ntnu.idatt2001.paths.gui;


/**
 * Represents a specific state within an application. E.g in a computer game, these states are usually the multiple
 * menus that build up the game; like "Main menu" or "New game".
 */
public interface ApplicationState {

    /**
     * Gives this ApplicationState a handle to the ApplicationStateHandler currently handling it. <br>
     * <br>
     * The ApplicationStateHandler running this ApplicationState is required to call this method
     * as soon as possible - with itself as the argument - whenever it switches to a new ApplicationState.
     * @param handlerRef The ApplicationStateHandler currently handling this ApplicationState
     */
    void setHandlerRef(ApplicationStateHandler handlerRef);

    /**
     * This method is called whenever an ApplicationStateHandler changes to this ApplicationState. <br>
     * This is to let the application state perform necessary initialization before the handler begins processing.
     */
    void setup();

    /**
     * This method is called whenever an ApplicationStateHandler changes away from this ApplicationState.
     * The idea is to give the ApplicationState an opportunity to de-initialize itself,
     * releasing system-resources or ending threads.
     */
    void cleanup();
}
