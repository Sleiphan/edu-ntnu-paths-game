package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.action.*;

import java.util.*;


public class PathsParser {

    private static final String CRLF = "\r\n";
    private static final String LF = "\n";
    private static final String PATHS_FILE_SPLITTER = LF + LF;
    private static final String ACTON_SEPARATOR = "}\\{";

    /**
     * Converts a Story-object into a String, using the format of .paths.
     * @param s The Story-object to be converted to a String.
     * @return The submitted Story-object as a .paths-formatted String.
     */
    public static String toPathsFormat(Story s) {
        if (s == null)
            throw new IllegalArgumentException("Argument cannot be null");

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

    /**
     * Reads a Story-object from a string. The format should be the same as the output of this methods'
     * sibling, namely <code>PathsParser::toPathsFormat(Story s)</code>
     * @param pathsString A string containing the valid format for a Story object.
     * @return A Story object as configured by the submitted string, or null if the parsing fails.
     */
    public static Story fromPathsFormatStory(String pathsString) {
        if (pathsString == null)
            return null;

        try {
            String dataSeparator = PATHS_FILE_SPLITTER;
            if (pathsString.contains(CRLF)) // If one occurrence of CRLF is found within this file, assume CRLF line ending.
                dataSeparator = CRLF + CRLF;

            String[] parts = pathsString.split(dataSeparator + "");
            if (parts.length < 2)
                return null; // A story must have both a title and an opening passage.

            String title = parts[0];
            Passage opening = fromPathsFormatPassage(parts[1]);
            if (opening == null)
                return null; // If the opening passage failed to parse, then this Story could not be parsed.
            Story story = new Story(title, opening);

            for (int i = 2; i < parts.length; i++) {
                Passage p = fromPathsFormatPassage(parts[i]);
                if (p == null)
                    return null; // If one of the passages fails to be parsed, this Story could not be parsed.
                story.addPassage(p);
            }

            return story;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a Passage-object into a String, using the format of .paths.
     * @param p The Passage-object to be converted to a String.
     * @return The submitted Passage-object as a .paths-formatted String.
     */
    public static String toPathsFormat(Passage p) {
        if (p == null)
            throw new IllegalArgumentException("Argument cannot be null");

        StringBuilder passageAsString = new StringBuilder();
        passageAsString.append("::").append(p.getTitle()).append('\n');
        passageAsString.append(p.getContent());
        for(Link l : p.getLinks()){
            passageAsString.append('\n').append(toPathsFormat(l));
        }
        return passageAsString.toString();
    }

    /**
     * Reads a Passage-object from a string. The format should be the same as the output of this methods'
     * sibling, namely <code>PathsParser::toPathsFormat(Passage p)</code>
     * @param pathsString A string containing the valid format for a Passage object.
     * @return A Passage object as configured by the submitted string, or null if the parsing fails.
     */
    public static Passage fromPathsFormatPassage(String pathsString) {
        if (pathsString == null)
            return null;

        try {
            String dataSeparator = LF;
            if (pathsString.contains(CRLF))
                dataSeparator = CRLF;

            String[] pathsStringSplit = pathsString.split(dataSeparator);
            if (pathsStringSplit.length < 2)
                return null; // Every passage must have a title and content.

            String title = pathsStringSplit[0];
            if (title.length() <= 2 || !title.contains("::"))
                return null; // The title cannot be empty.
            title = title.substring(2);
            String content = pathsStringSplit[1];
            if (content.length() == 0)
                return null; // The content cannot be empty.

            List<Link> linkList = new ArrayList<>();
            for (int i = 2; i < pathsStringSplit.length; i++) {
                Link l = fromPathsFormatLink(pathsStringSplit[i]);
                if (l == null)
                    return null; // If one link fails to parse, this Passage could not be parsed.
                linkList.add(l);
            }
            return new Passage(title, content, linkList);
        } catch (Exception e) { // Should any exception occur during parsing, this Passage could not be parsed.
            return null;
        }
    }

    /**
     * Converts a Link-object into a String, using the format of .paths.
     * @param l The Link-object to be converted to a String.
     * @return The submitted Link-object as a .paths-formatted String.
     */
    public static String toPathsFormat(Link l) {
        if (l == null)
            throw new IllegalArgumentException("Argument cannot be null");

        StringBuilder sb = new StringBuilder();
        sb.append("(").append(l.getText()).append(")");
        sb.append("[").append(l.getReference()).append("]");
        for (Action a : l.getActions())
            sb.append(toPathsFormat(a));
        return sb.toString();
    }

    /**
     * Reads a Link-object from a string. The format should be the same as the output of this methods'
     * sibling, namely <code>PathsParser::toPathsFormat(Link l)</code>
     * @param pathsString A string containing the valid format for a Link object.
     * @return A Link object as configured by the submitted string, or null if the parsing fails.
     */
    public static Link fromPathsFormatLink(String pathsString) {
        if (pathsString == null)
            return null;

        try {
            int index_1 = 1;
            int index_2 = pathsString.indexOf(")[", index_1);
            if (index_2 <= index_1)
                return null; // Every link must have a text.
            String text = pathsString.substring(index_1, index_2);

            index_1 = index_2 + 2;
            index_2 = pathsString.indexOf("]", index_1);
            if (index_2 <= index_1)
                return null; // Every link must have a reference.
            String ref = pathsString.substring(index_1, index_2);

            Link l = new Link(text, ref);

            index_1 = index_2 + 2;
            index_2 = pathsString.lastIndexOf('}');
            if (index_2 <= index_1) // Does this link have any actions?
                return l; // If not, return the link without any actions.

            String[] linksS = pathsString.substring(index_1, index_2).split(ACTON_SEPARATOR);

            Action a;
            for (String s : linksS) {
                a = fromPathsFormatAction("{" + s + "}");
                if (a == null)
                    return null; // If one action fails to be parsed, this Link could not be parsed.

                l.addAction(fromPathsFormatAction("{" + s + "}"));
            }

            return l;
        } catch (Exception e) { // Should any exception occur during parsing, this Link could not be parsed.
            return null;
        }
    }

    /**
     * Converts an Action-object into a String, <br> using the format of .paths. <br>
     * <br>
     * To find the correct conversion instruction, this method uses <code>instanceof</code> against known classes
     * that implement the Action interface.
     * These are the known classes: GoldAction, HealthAction, InventoryAction and ScoreAction. This method returns
     * <code>null</code> if the caller submits an instance of any unknown class that implements the Action interface.
     * @param a The Action-object to be converted to a String.
     * @return The submitted Link-object as a .paths-formatted String,
     * or <code>null</code> if an instance of an unknown Action-type is submitted.
     */
    public static String toPathsFormat(Action a) {
        if (a instanceof GoldAction)      return "{" +      GoldAction.class.getSimpleName() + ":" + toPathsFormat((GoldAction)      a) + "}";
        if (a instanceof HealthAction)    return "{" +    HealthAction.class.getSimpleName() + ":" + toPathsFormat((HealthAction)    a) + "}";
        if (a instanceof InventoryAction) return "{" + InventoryAction.class.getSimpleName() + ":" + toPathsFormat((InventoryAction) a) + "}";
        if (a instanceof ScoreAction)     return "{" +     ScoreAction.class.getSimpleName() + ":" + toPathsFormat((ScoreAction)     a) + "}";

        return null;
    }

    private static String toPathsFormat(GoldAction a) {
        return a.getGold() + "";
    }

    private static String toPathsFormat(HealthAction a) {
        return a.getHealth() + "";
    }

    private static String toPathsFormat(InventoryAction a) {
        return "\"" + a.getItem() + "\"";
    }

    private static String toPathsFormat(ScoreAction a) {
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
