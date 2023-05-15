package edu.ntnu.idatt2001.paths.gui;

/**
 * Handles a series of application states, where all states have something in common. <br>
 * <br>
 * For example, an in-game menu usually consists of multiple menus and submenus that are all
 * structured in different ways, but they may share a common way of handling graphics and receiving input. <br>
 * <br>
 * A menu-system based on frame-by-frame rendering, may want to include a render()-method
 * to let the currently active menu render itself on the screen.<br>
 * <br>
 * This interface should be considered to be usable at multiple levels of abstraction. A menu-system may contain another
 * system of menus with on of its submenus. That submenu (a type extending ApplicationState) would then hold
 * an instance of a class implementing this interface, running its own set of application states.<br>
 */
public interface ApplicationStateHandler {

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
    void changeState(ApplicationState newState);
}
