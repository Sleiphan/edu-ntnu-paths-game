package edu.ntnu.idatt2001.paths;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * A edu.ntnu.idatt2001.paths.Story is an interactive, nonlinear narrative consisting of a collection of passages.
 */
public class Story {

    private final String title;
    private final Passage openingPassage;
    private final Map<Link, Passage> passages = new HashMap<>();

    private int hashCode;
    private boolean recalculateHash;

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

        this.recalculateHash = true;
        hashCode();
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
     * If any previously added passages contains Link-objects referencing the submitted passage, a mapping between the objects is created.
     * This counts additionally for links within the submitted passage, referencing existing passages within this story.
     * <br><br>
     * If the passage has no associated links, a new link is added to the passage.
     * That new Link-object's title and reference is equal to the Passage-object's title, and contains no registered Action-objects.
     * @param p The passage to add to this story.
     */
    public void addPassage(Passage p) {
        boolean linked = false;
        Collection<Passage> existingPassagesCollection = getPassages();
        Passage[] existingPassages = existingPassagesCollection.toArray(new Passage[0]);
        List<Passage> existingPassagesWithDup = List.of(existingPassages);
        List<Passage> existingPassagesNoDup = new ArrayList<>(new HashSet<>(existingPassagesWithDup));

        for (Passage curr : existingPassagesNoDup) {
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

        // Add mapping between links in p and p.
        for (Link l : p.getLinks()) {
            if (l.getReference().equals(p.getTitle())) {
                passages.put(l, p);
                linked = true;
            }
        }

        if (!linked)
            passages.put(new Link(p.getTitle(), p.getTitle()), p);

        recalculateHash = true;
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

    public void removePassage(Link link){
        int instancesOfPassage = 0;
        Passage toBeRemoved = passages.get(link);

        if (toBeRemoved == null)
            throw new IllegalArgumentException("No passage mapped to the submitted link.");

        for(Passage p : passages.values()){
            if(p.equals(toBeRemoved)){
                boolean selfRef = false;
                for (Link l : toBeRemoved.getLinks())
                    if (l.equals(link)) {
                        selfRef = true;
                        break;
                    }

                if (!selfRef)
                    instancesOfPassage++;
            }
        }

        if (instancesOfPassage <= 1)
            passages.remove(link);

        recalculateHash = true;
    }

    /**
     * Gets a list of all links in the passages map that do not have an associated passage value
     * @return a list of all links in the passages map that do not have an associated passage value
     */
    public List<Link> getBrokenLinks(){
        List<Link> brokenLinks = new ArrayList<>();
        for(Map.Entry<Link,Passage> entry : passages.entrySet()){
            if(entry.getValue() == null){
                brokenLinks.add(entry.getKey());
            }
        }
        return brokenLinks;
    }

    /**
     * Adds a broken link to passages for testing purposes
     */
    public void addBrokenLink(){
        passages.put(new Link("Test","Test"),null);
        recalculateHash = true;
    }

    @Override
    public int hashCode() {
        if (recalculateHash) {
            hashCode = title.hashCode() * openingPassage.hashCode() * passages.hashCode();
            recalculateHash = false;
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Story))
            return false;

        Story s = (Story) o;
        return hashCode() == s.hashCode();
    }

    /**
     * Stores this story to the specified file.
     * @param file The file to store this story to.
     */
    public void saveToFile(Path path) throws IOException {
        Files.writeString(path, this.toPathsString());
    }

    public static Story readFromFile(Path path) throws IOException {
        String fileString = Files.readString(path);
        return Story.fromPathsString(fileString);
    }

    private static final String PATHS_FILE_SPLITTER = "\n\n";

    /**
     * @return
     */
    public String toPathsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle())
                .append(PATHS_FILE_SPLITTER)
                .append(getOpeningPassage().toPathsFormat());

        if (getPassages().size() <= 1)
            return sb.toString();

        // Create a collection of all passages with no duplicates.
        Set<Passage> passagesWithoutOpening = new HashSet<>(getPassages());
        passagesWithoutOpening.remove(getOpeningPassage());

        // Iterate over the Set we created in the last step, and add all of them to the output.
        for (Passage p : passagesWithoutOpening)
            sb.append(PATHS_FILE_SPLITTER).append(p.toPathsFormat());

        return sb.toString();
    }

    public static Story fromPathsString(String pathsString) {
        String[] parts = pathsString.split(PATHS_FILE_SPLITTER);
        String title = parts[0];
        Passage opening = Passage.fromPathsFormat(parts[1]);
        Story story = new Story(title, opening);

        for (int i = 2; i < parts.length; i++)
            story.addPassage(Passage.fromPathsFormat(parts[i]));

        return story;
    }
}