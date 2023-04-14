package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.action.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathsParser {

    private static final String PATHS_FILE_SPLITTER = "\n\n";
    private static final String ACTON_SEPARATOR = "}\\{";

    /**
     * @return
     */
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
            sb.append(a.toPathsFormat());
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
            l.addAction(Action.fromPathsFormat("{" + s + "}"));

        return l;
    }
}
