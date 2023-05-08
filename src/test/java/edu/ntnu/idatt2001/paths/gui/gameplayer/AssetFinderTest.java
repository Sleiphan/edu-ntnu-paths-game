package edu.ntnu.idatt2001.paths.gui.gameplayer;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.io.PathsParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AssetFinderTest {

    //@Test
    public void create_asset_file_from_story() throws IOException {
        String storyPath = "C:\\Users\\hfred\\source\\repos\\paths\\src\\main\\resources\\Stories\\test.paths";
        String resultPath = "C:\\Users\\hfred\\source\\repos\\paths\\src\\main\\resources\\Stories\\test.pathsassets";

        StringBuilder sb = new StringBuilder();
        sb.append(AssetFinder.generateGlobalTemplate());

        Story s = PathsParser.fromPathsFormatStory(Files.readString(Path.of(storyPath), StandardCharsets.UTF_8));

        for (Passage p : s.getPassages()) {
            String[] links = p.getLinks().stream().map(Link::getText).toArray(String[]::new);
            sb.append(AssetFinder.generatePassageTemplate(p.getTitle(), links));
        }

        Files.writeString(Path.of(resultPath), sb.toString(), StandardCharsets.UTF_8);
    }
}