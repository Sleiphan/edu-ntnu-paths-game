package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.action.*;

import java.util.*;


public class PathsParser {

    private static final String CRLF = "\r\n";
    private static final String LF = "\n";
    private static final String PATHS_FILE_SPLITTER = LF + LF;
    private static final String ACTON_SEPARATOR = "}\\{";

    private List<String> errors;
    private int layer = 0;
    private long line = 1;

    public PathsParser() {
        errors = new ArrayList<>();
    }



    public List<String> readAllErrors() {
        List<String> errors = this.errors;
        clearErrors();
        return errors;
    }

    public void clearErrors() {
        errors.clear();
    }

    private void errorBeforeReturn(String msg) {
        errors.add("Line " + line + ": " + msg);
        if (layer > 0)
            layer--;
    }

    private void error(String msg) {
        errors.add("Line " + line + ": " + msg);
    }

    /**
     * Converts a Story-object into a String, using the format of .paths.
     * @param s The Story-object to be converted to a String.
     * @return The submitted Story-object as a .paths-formatted String.
     */
    public String toPathsFormat(Story s) {
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
    public Story fromPathsFormatStory(String pathsString) {
        if (pathsString == null)
            return null;

        if (layer == 0)
            line = 1;

        layer++;

        try {
            String dataSeparator = PATHS_FILE_SPLITTER;
            if (pathsString.contains(CRLF)) // If one occurrence of CRLF is found within this file, assume CRLF line ending.
                dataSeparator = CRLF + CRLF;

            String[] parts = pathsString.split(dataSeparator + "");
            if (parts.length < 2) {
                errorBeforeReturn("Failed to find the title and the opening passage, separated by an empty line (double line shift). A story must have both a title and at least one passage.");
                return null; // A story must have both a title and an opening passage.
            }

            String title = parts[0];
            line += 2; // Successfully parsed the title.

            Passage opening = fromPathsFormatPassage(parts[1]);
            if (opening == null) {
                layer--;
                return null; // If the opening passage failed to parse, then this Story could not be parsed.
            }

            Story story = new Story(title, opening);

            boolean errorOccurred = false;
            for (int i = 2; i < parts.length; i++) {
                line += 2; // There are two line shifts between each passage
                Passage p = fromPathsFormatPassage(parts[i]);
                if (p != null)
                    story.addPassage(p);
                else
                    errorOccurred = true; // If one of the passages fails to be parsed, this Story could not be parsed.
            }

            layer--;

            if (errorOccurred)
                return null;

            return story;
        } catch (Exception e) {
            errorBeforeReturn("Unknown exception occurred while parsing a story: \"" + pathsString + "\"\n" + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    /**
     * Converts a Passage-object into a String, using the format of .paths.
     * @param p The Passage-object to be converted to a String.
     * @return The submitted Passage-object as a .paths-formatted String.
     */
    public String toPathsFormat(Passage p) {
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
    public Passage fromPathsFormatPassage(String pathsString) {
        if (pathsString == null)
            return null;

        if (layer == 0)
            line = 1;
        layer++;

        try {
            String dataSeparator = LF;
            if (pathsString.contains(CRLF))
                dataSeparator = CRLF;

            String[] pathsStringSplit = pathsString.split(dataSeparator);
            if (pathsStringSplit.length < 2) {
                errorBeforeReturn("Failed to find separation between passage title and content: \"" + pathsString + "\"");
                return null; // Every passage must have a title and content.
            }

            boolean errorOccurred = false;

            String title = pathsStringSplit[0];
            if (!title.contains("::")) {
                error("Could not find passage-symbol \"::\" in this passage: \"" + pathsString + "\"");
                errorOccurred = true; // The title cannot be empty.
            }
            else if (title.length() <= 2) {
                error("The title of this passage is empty: \"" + pathsString + "\"");
                errorOccurred = true; // The title cannot be empty.
            }

            line++;

            title = title.substring(2);
            String content = pathsStringSplit[1];
            if (content.length() == 0) {
                error("The content of this passage is empty: \"" + pathsString + "\"");
                errorOccurred = true; // The content cannot be empty.
            }

            List<Link> linkList = new ArrayList<>();
            for (int i = 2; i < pathsStringSplit.length; i++) {
                line++; // Every link is separated by one line.
                Link l = fromPathsFormatLink(pathsStringSplit[i]);
                if (l != null)
                    linkList.add(l);
                else
                    errorOccurred = true; // If one link fails to parse, this Passage could not be parsed.
            }

            layer--;

            if (errorOccurred)
                return null;

            return new Passage(title, content, linkList);
        } catch (Exception e) { // Should any exception occur during parsing, this Passage could not be parsed.
            errorBeforeReturn("Unknown exception occurred while parsing a passage: \"" + pathsString + "\"\n" + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    /**
     * Converts a Link-object into a String, using the format of .paths.
     * @param l The Link-object to be converted to a String.
     * @return The submitted Link-object as a .paths-formatted String.
     */
    public String toPathsFormat(Link l) {
        if (l == null)
            throw new IllegalArgumentException("Argument cannot be null");

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(l.getText()).append("]");
        sb.append("(").append(l.getReference()).append(")");
        for (Action a : l.getActions())
            sb.append(toPathsFormat(a));
        if (l.getScript() != null)
            sb.append("$").append(l.getScript()).append("$");
        if (l.getCondition() != null)
            sb.append("@").append(l.getCondition()).append("@");
        return sb.toString();
    }

    /**
     * Reads a Link-object from a string. The format should be the same as the output of this methods'
     * sibling, namely <code>PathsParser::toPathsFormat(Link l)</code>
     * @param pathsString A string containing the valid format for a Link object.
     * @return A Link object as configured by the submitted string, or null if the parsing fails.
     */
    public Link fromPathsFormatLink(String pathsString) {
        if (pathsString == null)
            return null;

        if (layer == 0)
            line = 1;
        layer++;

        try {
            int index_1 = 1;
            int index_2 = pathsString.indexOf("](", index_1);
            if (index_2 <= index_1) {
                errorBeforeReturn("Failed to find the description text of this link: \"" + pathsString + "\"");
                return null; // Every Link must have a text
            }
            String text = pathsString.substring(index_1, index_2);

            index_1 = index_2 + 2;
            index_2 = pathsString.indexOf(")", index_1);
            if (index_2 <= index_1) {
                errorBeforeReturn("Failed to find the passage reference in this link: \"" + pathsString + "\"");
                return null; // Every Link must have a reference
            }
            String ref = pathsString.substring(index_1, index_2);
            Link l = new Link(text, ref);

            boolean errorOccurred = false;

            index_1 = pathsString.indexOf('{', index_2) + 1;
            index_2 = pathsString.lastIndexOf('}');
            if (index_2 > index_1) { // Does this link have any actions?
                String[] linksS = pathsString.substring(index_1, index_2).split(ACTON_SEPARATOR);

                for (String s : linksS) {
                    Action a = fromPathsFormatAction("{" + s + "}");
                    if (a != null)
                        l.addAction(fromPathsFormatAction("{" + s + "}"));
                    else
                        errorOccurred = true; // If one action fails to be parsed, this Link could not be parsed.
                }
            }

            index_1 = pathsString.indexOf('$', index_2) + 1;
            index_2 = pathsString.indexOf('$', index_1 + 1);
            if (index_2 > index_1) { // Does this link have a script?
                String script = pathsString.substring(index_1, index_2);
                l.setScript(script);
            }

            index_1 = pathsString.indexOf('@', index_2) + 1;
            index_2 = pathsString.indexOf('@', index_1 + 1);
            if (index_2 > index_1) { // Does this link have a condition?
                String condition = pathsString.substring(index_1, index_2);
                l.setCondition(condition);
            }

            layer--;
            if (errorOccurred)
                return null;

            return l;
        } catch (Exception e) { // Should any exception occur during parsing, this Link could not be parsed.
            errorBeforeReturn("Unknown exception occurred while parsing a link: \"" + pathsString + "\"\n" + Arrays.toString(e.getStackTrace()));
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
    public String toPathsFormat(Action a) {
        if (a instanceof GoldAction)      return "{" +      GoldAction.class.getSimpleName() + ":" + toPathsFormat((GoldAction)      a) + "}";
        if (a instanceof HealthAction)    return "{" +    HealthAction.class.getSimpleName() + ":" + toPathsFormat((HealthAction)    a) + "}";
        if (a instanceof InventoryAction) return "{" + InventoryAction.class.getSimpleName() + ":" + toPathsFormat((InventoryAction) a) + "}";
        if (a instanceof ScoreAction)     return "{" +     ScoreAction.class.getSimpleName() + ":" + toPathsFormat((ScoreAction)     a) + "}";

        return null;
    }

    private String toPathsFormat(GoldAction a) {
        return a.getGold() + "";
    }

    private String toPathsFormat(HealthAction a) {
        return a.getHealth() + "";
    }

    private String toPathsFormat(InventoryAction a) {
        return "\"" + (a.addsItem() ? "" : "-") + a.getItem() + "\"";
    }

    private String toPathsFormat(ScoreAction a) {
        return a.getPoints() + "";
    }


    public Action fromPathsFormatAction(String pathsString) {
        if (pathsString == null)
            return null;

        if (layer == 0)
            line = 1;
        layer++;

        if (!pathsString.startsWith("{") || !pathsString.endsWith("}")) {
            errorBeforeReturn("This action seems to not be encased in {}-parenthesis: " + pathsString);
            return null;
        }

        pathsString = pathsString.substring(1, pathsString.length() - 1); // Remove the appending and prepending curly brackets.
        pathsString = pathsString.replaceAll("(?!\\s)", ""); // Remove any unnecessary white-space in the data, ignoring content within string literals.

        final String[] actionClassNames = new String[] {
                     GoldAction.class.getSimpleName(),
                   HealthAction.class.getSimpleName(),
                InventoryAction.class.getSimpleName(),
                    ScoreAction.class.getSimpleName()
        };

        int delimiterIndex = pathsString.indexOf(':');
        if (delimiterIndex == -1 || delimiterIndex >= pathsString.length() - 1)  { // If we could not find the delimiter, or if the delimiter is the last character in the input data...
            errorBeforeReturn("Could not find delimiter for this action: " + pathsString); // ... signal that the data is invalid.
            return null;
        }

        // Split only once. No guarantee that the delimiter shows up in the value-part of the data.
        String[] parts = new String[] {
                pathsString.substring(0, delimiterIndex),
                pathsString.substring(delimiterIndex + 1)
        };

        String key = parts[0];
        String val = parts[1];

        boolean keyValid = Arrays.asList(actionClassNames).contains(key);
        if (!keyValid) {
            errorBeforeReturn("Unknown action type: " + key);
            return null;
        }

        Action a = null;

        if (key.equals(     GoldAction.class.getSimpleName())) a = fromPathsFormatGoldAction     (val);
        if (key.equals(   HealthAction.class.getSimpleName())) a = fromPathsFormatHealthAction   (val);
        if (key.equals(InventoryAction.class.getSimpleName())) a = fromPathsFormatInventoryAction(val);
        if (key.equals(    ScoreAction.class.getSimpleName())) a = fromPathsFormatScoreAction    (val);

        layer--;
        return a;
    }

    private GoldAction fromPathsFormatGoldAction(String pathsString) {
        layer++;

        Integer val = tryParseInt(pathsString);
        if (val == null) {
            errorBeforeReturn("Failed to parse value of GoldAction. Found value: " + pathsString);
            return null;
        }

        layer--;
        return new GoldAction(val);
    }

    private HealthAction fromPathsFormatHealthAction(String pathsString) {
        layer++;

        Integer val = tryParseInt(pathsString);
        if (val == null) {
            errorBeforeReturn("Failed to parse value of HealthAction. Found value: " + pathsString);
            return null;
        }

        layer--;
        return new HealthAction(val);
    }

    private InventoryAction fromPathsFormatInventoryAction(String pathsString) {
        layer++;

        if (!pathsString.startsWith("\"") || !pathsString.endsWith("\"")) {
            errorBeforeReturn("Item name must be encased in \"-characters. Found value: " + pathsString);
            return null;
        }

        String item = pathsString.substring(1, pathsString.length() - 1);

        boolean add = true;
        if (item.startsWith("-")) {
            add = false;
            item = item.substring(1);
        }

        layer--;
        return new InventoryAction(item, add);
    }

    private ScoreAction fromPathsFormatScoreAction(String pathsString) {
        layer++;

        Integer val = tryParseInt(pathsString);
        if (val == null) {
            errorBeforeReturn("Failed to parse value of ScoreAction. Found value: " + pathsString);
            return null;
        }

        layer--;
        return new ScoreAction(val);
    }

    private Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
