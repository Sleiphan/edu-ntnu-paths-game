package edu.ntnu.idatt2001.paths.gui;


/**
 * Represents a specific state within an application. E.g in a computer game, these states are usually the multiple
 * menus that build up the game; like "Main menu" or "New game".
 */
public interface ApplicationState<HANDLER_TYPE extends ApplicationStateHandler> {

    /**
     * Gives this ApplicationState a handle to the ApplicationStateHandler currently handling it. <br>
     * <br>
     * The ApplicationStateHandler running this ApplicationState is required to call this method
     * as soon as possible - with itself as the argument - whenever it switches to a new ApplicationState.
     * @param handlerRef The ApplicationStateHandler currently handling this ApplicationState
     */
    void setHandlerRef(HANDLER_TYPE handlerRef);

    /**
     * Call this method to switch application state. This method calls the current application state's cleanup-method,
     * changes the current state to the submitted new state, and calls the setup-method of the new state. <br>
     * <br>
     * In canon, this method would be called when the user presses a button on a menu that opens a submenu.
     * To perform the changeover to the requested submenu, the button would call this method to
     * de-initialise its own menu and start up the new submenu. This kind of structure requires the button object to
     * take into account that its associated menu will perform de-initialisation, and possibly de-reference
     * the button-object during this method call.<br>
     *
     * @param newState The new application state to be the new current state.
     */
    void changeState(ApplicationState<HANDLER_TYPE> newState);

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
