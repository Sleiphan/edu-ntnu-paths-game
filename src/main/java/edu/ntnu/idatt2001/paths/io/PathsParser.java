package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.action.*;

import java.util.*;

public class PathsParser {

    private static final String PATHS_FILE_SPLITTER = "\n\n";
    private static final String ACTON_SEPARATOR = "}\\{";

    public static String toPathsFormat(Story s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s.getTitle())
                .append(PATHS_FILE_SPLITTER)
                .append(toPathsFormat(s.getOpeningPassage()));

        if (s.getPassages().size() <= 1)
            return sb.toString();

        // Create a collection of all passages with no duplicates.
        Set<Passage> passagesWithoutOpening = new HashSet<>(s.getPassages());
        passagesWithoutOpening.remove(s.getOpeningPassage());

        // Iterate over the Set we created in the last step, and add all of them to the output.
        for (Passage p : passagesWithoutOpening)
            sb.append(PATHS_FILE_SPLITTER).append(toPathsFormat(p));

        return sb.toString();
    }

    public static Story fromPathsFormatStory(String pathsString) {
        String[] parts = pathsString.split(PATHS_FILE_SPLITTER);
        String title = parts[0];
        Passage opening = fromPathsFormatPassage(parts[1]);
        Story story = new Story(title, opening);

        for (int i = 2; i < parts.length; i++)
            story.addPassage(fromPathsFormatPassage(parts[i]));

        return story;
    }

    /**
     * Converts the passage to a string that can be written to the .paths format
     * @return the passage as a string
     */
    public static String toPathsFormat(Passage p) {
        StringBuilder passageAsString = new StringBuilder();
        passageAsString.append("::").append(p.getTitle()).append('\n');
        passageAsString.append(p.getContent());
        for(Link l : p.getLinks()){
            passageAsString.append('\n').append(toPathsFormat(l));
        }
        return passageAsString.toString();
    }

    /**
     * Reads a passage from a string
     * @param pathsString string to read passage from
     * @return            Passage found in string
     */
    public static Passage fromPathsFormatPassage(String pathsString) {
        String[] pathsStringSplit = pathsString.split("\n");
        String title = pathsStringSplit[0].substring(2);
        String content = pathsStringSplit[1];
        List<Link> linkList = new ArrayList<>();
        for(int i = 2; i < pathsStringSplit.length; i++){
            linkList.add(fromPathsFormatLink(pathsStringSplit[i]));
        }
        return new Passage(title,content,linkList);
    }


    public static String toPathsFormat(Link l) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(l.getText()).append(")");
        sb.append("[").append(l.getReference()).append("]");
        for (Action a : l.getActions())
            sb.append(toPathsFormat(a));
        return sb.toString();
    }

    public static Link fromPathsFormatLink(String pathsString) {
        int index_1 = 1;
        int index_2 = pathsString.indexOf(")[", index_1);
        String text = pathsString.substring(index_1, index_2);

        index_1 = index_2 + 2;
        index_2 = pathsString.indexOf("]", index_1);
        String ref = pathsString.substring(index_1, index_2);

        Link l = new Link(text, ref);

        if (pathsString.length() <= index_2 + 1)
            return l;

        String[] linksS = pathsString.substring(index_2 + 2, pathsString.length() - 1).split(ACTON_SEPARATOR);

        for (String s : linksS)
            l.addAction(fromPathsFormatAction("{" + s + "}"));

        return l;
    }

    public static String toPathsFormat(Action a) {
        if (a instanceof GoldAction)      return "{" +      GoldAction.class.getSimpleName() + ":" + toPathsFormat((GoldAction)      a) + "}";
        if (a instanceof HealthAction)    return "{" +    HealthAction.class.getSimpleName() + ":" + toPathsFormat((HealthAction)    a) + "}";
        if (a instanceof InventoryAction) return "{" + InventoryAction.class.getSimpleName() + ":" + toPathsFormat((InventoryAction) a) + "}";
        if (a instanceof ScoreAction)     return "{" +     ScoreAction.class.getSimpleName() + ":" + toPathsFormat((ScoreAction)     a) + "}";

        return null;
    }

    public static String toPathsFormat(GoldAction a) {
        return a.getGold() + "";
    }

    public static String toPathsFormat(HealthAction a) {
        return a.getHealth() + "";
    }

    public static String toPathsFormat(InventoryAction a) {
        return "\"" + a.getItem() + "\"";
    }

    public static String toPathsFormat(ScoreAction a) {
        return a.getPoints() + "";
    }


    public static Action fromPathsFormatAction(String pathsString) {
        if (pathsString == null)
            return null;

        if (!pathsString.startsWith("{") || !pathsString.endsWith("}"))
            return null;

        pathsString = pathsString.substring(1, pathsString.length() - 1); // Remove the appending and prepending curly brackets.
        pathsString = pathsString.replaceAll("(?!\\s)", ""); // Remove any unnecessary white-space in the data, ignoring content within string literals.

        final String[] actionClassNames = new String[]{
                     GoldAction.class.getSimpleName(),
                   HealthAction.class.getSimpleName(),
                InventoryAction.class.getSimpleName(),
                    ScoreAction.class.getSimpleName()
        };

        int delimiterIndex = pathsString.indexOf(':');
        if (delimiterIndex == -1 || delimiterIndex >= pathsString.length() - 1) // If we could not find the delimiter, or if the delimiter is the last character in the input data...
            return null; // ... signal that the data is invalid.


        // Split only once. No guarantee that the delimiter shows up in the value-part of the data.
        String[] parts = new String[] {
                pathsString.substring(0, delimiterIndex),
                pathsString.substring(delimiterIndex + 1)
        };

        String key = parts[0];
        String val = parts[1];

        boolean keyValid = Arrays.asList(actionClassNames).contains(key);
        if (!keyValid)
            return null;

        if (key.equals(     GoldAction.class.getSimpleName())) return fromPathsFormatGoldAction     (val);
        if (key.equals(   HealthAction.class.getSimpleName())) return fromPathsFormatHealthAction   (val);
        if (key.equals(InventoryAction.class.getSimpleName())) return fromPathsFormatInventoryAction(val);
        if (key.equals(    ScoreAction.class.getSimpleName())) return fromPathsFormatScoreAction    (val);

        return null;
    }

    private static GoldAction fromPathsFormatGoldAction(String pathsString) {
        Integer val = tryParseInt(pathsString);
        if (val == null)
            return null;
        return new GoldAction(val);
    }

    private static HealthAction fromPathsFormatHealthAction(String pathsString) {
        Integer val = tryParseInt(pathsString);
        if (val == null)
            return null;
        return new HealthAction(val);
    }

    private static InventoryAction fromPathsFormatInventoryAction(String pathsString) {
        if (!pathsString.startsWith("\"") || !pathsString.endsWith("\""))
            return null;
        return new InventoryAction(pathsString.substring(1, pathsString.length() - 1));
    }

    private static ScoreAction fromPathsFormatScoreAction(String pathsString) {
        Integer val = tryParseInt(pathsString);
        if (val == null)
            return null;
        return new ScoreAction(val);
    }

    private static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
