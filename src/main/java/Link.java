import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Link creates a connection between two passages, and binds together parts of a story.
 */
public class Link {
    
    private final String text;
    private final String reference;
    private final List<Action> actions;

    private int hashCode;
    private boolean recalculateHash;

    /**
     * Creates a new Link-object. A Link is a connection between two passages, and binds together parts of a story.
     * @param text A description indicating a choice or action in the story.
     * @param reference A unique String identifying a Passage object. Usually the title of the passage.
     */
    public Link(String text, String reference) {
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
     * Returns this Link-object's unique String identifying a Passage-object.
     * @return this Link-object's unique String identifying a Passage-object.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Adds a new action to the list of actions to choose from.
     * The submitted Action-object is only added if no other Action-object with the same state has been added earlier.
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
     * Returns a deep copy of the internal list of available actions in this Link-object.
     * @return a deep copy of the internal list of available actions in this Link-object.
     */
    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    /**
     * Returns a String containing a comprehensive overview of this Link-object.
     * @return a String containing a comprehensive overview of this Link-object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Link, with reference: \"").append(reference).append("\"\n");
        sb.append("Text:\n\"").append(text).append("\"\n");
        sb.append("Possible actions:");
        for (Action a : actions)
            sb.append("\n").append("    ").append(a.toString());
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
            recalculateHash = false;
        }
        return hashCode;
    }
}
