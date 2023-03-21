package edu.ntnu.idatt2001.paths;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A edu.ntnu.idatt2001.paths.Story is an interactive, nonlinear narrative consisting of a collection of passages.
 */
public class Story {

    private final String title;
    private final Passage openingPassage;
    private final Map<Link, Passage> passages = new HashMap<>();

    /**
     * Creates a new edu.ntnu.idatt2001.paths.Story-object, with a title and an initial passage from where the story begins.
     * @param title The title of the new story.
     * @param openingPassage The initial passage from where the story begins.
     */
    public Story(String title, Passage openingPassage) {
        if (title == null)
            throw new IllegalArgumentException("The title of a story cannot be null");
        if (openingPassage == null)
            throw new IllegalArgumentException("The opening passage of a story cannot be null");

        this.title = title;
        this.openingPassage = openingPassage;

        addPassage(openingPassage);
    }

    /**
     * Returns the title of this story.
     * @return the title of this story.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the initial passage of this story.
     * @return the initial passage of this story.
     */
    public Passage getOpeningPassage() {
        return openingPassage;
    }

    /**
     * Adds a new passage to this story.
     * <br><br>
     * If any previously added passages contains edu.ntnu.idatt2001.paths.Link-objects referencing the submitted passage, a mapping between the objects is created.
     * This counts additionally for links within the submitted passage, referencing existing passages within this story.
     * <br><br>
     * If the passage has no associated links, a new link is added to the passage.
     * That new edu.ntnu.idatt2001.paths.Link-object's title and reference is equal to the edu.ntnu.idatt2001.paths.Passage-object's title, and contains no registered edu.ntnu.idatt2001.paths.action.Action-objects.
     * @param p The passage to add to this story.
     */
    public void addPassage(Passage p) {
        boolean linked = false;

        for (Passage curr : getPassages()) {
            // Add mapping between links in existing passages and p.
            for (Link l : curr.getLinks())
                if (l.getReference().equals(p.getTitle())) {
                    passages.put(l, p);
                    linked = true;
                }
            // Add mapping between links in p and existing passages.
            for (Link l : p.getLinks())
                if (l.getReference().equals(curr.getTitle()))
                    passages.put(l, curr);
        }

        if (!linked)
            passages.put(new Link(p.getTitle(), p.getTitle()), p);
    }

    /**
     * Returns the passage being linked to by the submitted edu.ntnu.idatt2001.paths.Link-object.
     * @param l The edu.ntnu.idatt2001.paths.Link-object pointing to a specific passage.
     * @return the passage being linked to by the submitted edu.ntnu.idatt2001.paths.Link-object.
     */
    public Passage getPassage(Link l) {
        return passages.get(l);
    }

    /**
     * Returns a collection of all the passages in this story.
     * @return a collection of all the passages in this story.
     */
    public Collection<Passage> getPassages() {
        return passages.values();
    }
}
