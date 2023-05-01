package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for loading all parts of a game story, including the story itself and assets.
 */
public class StoryLoader {

    public static final String PATHS_FILE_ENDING = ".paths";
    public static final String PATHS_ASSET_FILE_ENDING = ".pathsassets";

    private final Path storyFilePath;
    private final String workingDirectory;
    private final Path assetsFilePath;
    private final List<String> errors = new ArrayList<>();

    private Story story;
    private PathsAssetStore assetStore;

    private boolean foundAssetStore;

    public StoryLoader(String storyFilePath) throws FileNotFoundException {
        storyFilePath = storyFilePath.replaceAll("\\\\", "/");

        if (!storyFilePath.endsWith(PATHS_FILE_ENDING))
            throw new IllegalArgumentException("The story file must end with \""+ PATHS_FILE_ENDING + "\"");

        this.storyFilePath = Path.of(storyFilePath);
        if (!Files.exists(this.storyFilePath))
            throw new FileNotFoundException("Failed to find file: " + storyFilePath);

        workingDirectory = storyFilePath.substring(0, storyFilePath.lastIndexOf('/') + 1);

        String assetsPathString = storyFilePath.substring(0, storyFilePath.lastIndexOf('.')) + PATHS_ASSET_FILE_ENDING;
        this.assetsFilePath = Path.of(assetsPathString);
        if (Files.exists(this.assetsFilePath))
            foundAssetStore = true;
    }

    public boolean load() {
        return load(Charset.defaultCharset());
    }

    public boolean load(Charset charset) {
        String data;
        try {
            data = Files.readString(storyFilePath, charset);
        } catch (IOException e) {
            errors.add("Unknown error reading data from story file: " + e);
            return false;
        }

        this.story = PathsParser.fromPathsFormatStory(data);

        boolean success = true;

        if (foundAssetStore)
            success = loadAssetStore(charset);

        return success;
    }

    private boolean loadAssetStore(Charset charset) {
        PathsAssetStoreParser parser = new PathsAssetStoreParser();
        String data;
        try {
            data = Files.readString(assetsFilePath, charset);
        } catch (IOException e) {
            errors.add("Unknown error reading data from asset file: " + e);
            return false;
        }

        this.assetStore = parser.parsePathsAssetStore(data, workingDirectory);

        String[] errors = parser.getErrorsFromLastParse();
        if (errors != null) {
            this.errors.addAll(List.of(errors));
            return false;
        }
        else
            return true;
    }

    public boolean foundAssetStore() {
        return foundAssetStore;
    }

    public Story getStory() {
        return story;
    }

    public PathsAssetStore getAssetStore() {
        return assetStore;
    }

    public boolean errorsOccurred() {
        return !errors.isEmpty();
    }

    public String[] getErrors() {
        if (errorsOccurred())
            return errors.toArray(String[]::new);
        else
            return null;
    }
}
