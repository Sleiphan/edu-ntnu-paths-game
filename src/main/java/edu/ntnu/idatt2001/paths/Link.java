package edu.ntnu.idatt2001.paths;

import edu.ntnu.idatt2001.paths.action.Action;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * A edu.ntnu.idatt2001.paths.Link creates a connection between two passages, and binds together parts of a story.
 */
public class Link {
    
    private final String text;
    private final String reference;
    private final List<Action> actions;
    private String script;
    private String condition;

    private int hashCode;
    private boolean recalculateHash;

    /**
     * Creates a new Link-object. A Link is a connection between two passages, and binds together parts of a story.
     * @param text A description indicating a choice or action in the story.
     * @param reference A unique String identifying a Passage object. Usually the title of the passage.
     */
    public Link(String text, String reference) {
        if (text == null)
            throw new IllegalArgumentException("A Link-object's text cannot be null");
        if (reference == null)
            throw new IllegalArgumentException("A Link-object's reference cannot be null");

        this.text = text;
        this.reference = reference;
        actions = new ArrayList<>();

        // Make sure the hashCode is calculated.
        recalculateHash = true;
        this.hashCode();
    }

    /**
     * Returns this Link-object's associated description, indicating a choice or action in the story.
     * @return this Link-object's associated description, indicating a choice or action in the story.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns this edu.ntnu.idatt2001.paths.Link-object's unique String identifying a edu.ntnu.idatt2001.paths.Passage-object. Usually contains the title of the passage being pointed to.
     * @return this edu.ntnu.idatt2001.paths.Link-object's unique String identifying a edu.ntnu.idatt2001.paths.Passage-object.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Adds a new action to the list of actions to choose from.
     * The submitted edu.ntnu.idatt2001.paths.action.Action-object is only added if no other edu.ntnu.idatt2001.paths.action.Action-object with the same state has been added earlier.
     * This method changes this object's hashcode, and thus, marks the hashcode for recalculation.
     * @param action The new action to be added to the list of actions to choose from.
     * @return true if the submitted action-object was added, otherwise returns false.
     */
    public boolean addAction(Action action) {
        for (Action a : actions) // Iterate over existing actions
            if (a.equals(action)) // Is the new action already present among the actions?
                return false; // Do not add a duplicate of the same action.

        // If no duplicate was found...
        actions.add(action); // Add the new action,
        recalculateHash = true; // recalculate the hashcode,
        return true; // and return success-status to the caller.
    }

    /**
     * Returns a reference to the internal list of available actions in this edu.ntnu.idatt2001.paths.Link-object.
     * @return a reference to the internal list of available actions in this edu.ntnu.idatt2001.paths.Link-object.
     */
    public List<Action> getActions() {
        return actions;
        //return new ArrayList<>(actions);
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void runScript(ScriptEngine engine) {
        if (script != null) {
            try {
                engine.eval(script);
            } catch (ScriptException e) {
                return; // For now, ignore these exceptions.
            }
        }
    }

    public boolean isLinkOpen(ScriptEngine engine) {
        if (condition == null)
            return true;

        try {
            return (Boolean) engine.eval(condition);
        } catch (ScriptException e) {
            return true; // For now, ignore these exceptions.
        }
    }

    /**
     * Returns a String containing a comprehensive overview of this Link-object.
     * @return a String containing a comprehensive overview of this Link-object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Link {\n    reference: \"").append(reference).append("\"\n");
        sb.append("    text:\"").append(text).append("\"\n");

        if (actions.size() > 0) {
            sb.append("    Possible actions: [ ").append(actions.get(0));
            for (Action a : actions)
                sb.append(", ").append(a.toString());
            sb.append(" ]");
        }

        return sb.toString();
    }

    /**
     * Compares the state of this object to that of the submitted parameter object.
     * @param link The Link-object to compare this object to.
     * @return true if - and only if - the parameter 'link' is an instance of this Link-class <i>AND</i> this object's state is equal to that of the parameter. Else, returns false.
     */
    @Override
    public boolean equals(Object link) {
        if (link instanceof Link)
            return hashCode() == link.hashCode();
        else return false;
    }

    /**
     * <h1>hashCode()</h1>
     * Returns the hashcode of this object. This hashcode is calculated by multiplying the hashcodes of this class's fields together.
     * <br><br>
     * <h3>Optimisation</h3>
     * Calculation of the hashcode happens in this method, but only if changes to this object's state has been made.
     * This allows for faster successive access to this attribute.
     * @return the hashcode of this object.
     */
    @Override
    public int hashCode() {
        if (recalculateHash) {
            hashCode = text.hashCode() * reference.hashCode() * actions.hashCode();
            if (script != null)
                hashCode *= script.hashCode();
            if (condition != null)
                hashCode *= condition.hashCode();
            recalculateHash = false;
        }
        return hashCode;
    }
}
