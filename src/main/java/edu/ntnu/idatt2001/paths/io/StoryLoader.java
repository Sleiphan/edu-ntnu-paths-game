package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class' objective is to load a complete story - along with supplementary elements like assets -
 * from the file system, and store them together in a convenient way.<br>
 * <br>
 * As an outset, the StoryLoader is guaranteed to load a Story-object that can be run as a game, given that
 * the contents of the file properly adhere to the .paths-format.
 * The loader will try to uncover and load additional resources in the same directory as the .paths-file
 * containing the story. <br>
 * <br>
 * Any errors that occur during loading can be retrieved by calling
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
    private PathsParser parser = new PathsParser();

    private boolean foundAssetStore;

    /**
     * Creates a new StoryLoader for use on a single Story-file.
     * @param storyFilePath The path to the file containing a Story, with contents adhering to the .paths-format.
     * @throws FileNotFoundException if the file pointed to by <code>storyFilePath</code> could not be found.
     * @throws IllegalArgumentException if the file pointed to by <code>storyFilePath</code> does not end with ".paths".
     */
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

    /**
     * Loads the Story, and attempts to load additional elements connected to the Story, like assets.
     * Uses Charset.defaultCharset() as the character encoding for converting data from files to String-objects.<br>
     * <br>
     * This method returns <code>true</code> if all discovered elements were successfully loaded. This means that the
     * method returns <code>false</code> even if a Story-object was successfully loaded, but a discovered asset store could
     * not be successfully loaded. If a Story was successfully loaded, and no other elements were discovered,
     * this method returns <code>true</code>.
     * @return <code>true</code> if all discovered elements were successfully loaded. Otherwise, returns
     * <code>false</code>.
     */
    public boolean load() {
        return load(Charset.defaultCharset());
    }

    /**
     * Loads the Story, and attempts to load additional elements connected to the Story, like assets. <br>
     * <br>
     * This method returns <code>true</code> if all discovered elements were successfully loaded. This means that the
     * method returns <code>false</code> even if a Story-object was successfully loaded, but a discovered asset store could
     * not be successfully loaded. If a Story was successfully loaded, and no other elements were discovered,
     * this method returns <code>true</code>.
     * @param charset The character encoding of the file, e.g. UTF-8. Although not recommended, use
     *                'Charset.defaultCharset()' if you have no way of knowing the file's encoding.
     * @return <code>true</code> if all discovered elements were successfully loaded. Otherwise, returns
     * <code>false</code>.
     */
    public boolean load(Charset charset) {
        boolean success = loadStory(storyFilePath, charset);

        if (foundAssetStore)
            success = loadAssetStore(assetsFilePath, charset);

        return success;
    }

    /**
     * Loads a story.
     * @param charset The character encoding of the file, e.g. UTF-8. Although not recommended, use
     *                'Charset.defaultCharset()' if you have no way of knowing the file's encoding.
     * @return <code>true</code> if the story was successfully loaded.
     */
    private boolean loadStory(Path storyFilePath, Charset charset) {
        String data;
        try {
            data = Files.readString(storyFilePath, charset);
        } catch (IOException e) {
            errors.add("Unknown error reading data from story file: " + e);
            return false;
        }

        story = parser.fromPathsFormatStory(data);
        errors.addAll(parser.readAllErrors());

        return story != null;
    }

    /**
     * Loads an asset store.
     * @param charset The character encoding of the file, e.g. UTF-8. Although not recommended, use
     *                'Charset.defaultCharset()' if you have no way of knowing the file's encoding.
     * @return <code>true</code> if the asset store was successfully loaded.
     */
    public boolean loadAssetStore(Path assetRegisterPath, Charset charset) {
        PathsAssetStoreParser parser = new PathsAssetStoreParser();
        String data;
        try {
            data = Files.readString(assetRegisterPath, charset);
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

    /**
     * Indicates whether an asset store has been successfully loaded.
     * @return <code>true</code> if an asset store has been loaded and is available. Otherwise, returns <code>false</code>.
     */
    public boolean foundAssetStore() {
        return foundAssetStore;
    }

    /**
     * Returns the Story-object resulting from a call to <code>this::load</code>.
     * @return the Story-object resulting from a call to <code>this::load</code>.
     */
    public Story getStory() {
        return story;
    }


    public PathsAssetStore getAssetStore() {
        return assetStore;
    }

    /**
     * Indicates whether there are unread errors stored in this class from loading operations.
     * @return <code>true</code> if there are unread errors stored. Otherwise, returns <code>false</code>.
     */
    public boolean errorsPending() {
        return !errors.isEmpty();
    }

    /**
     * Returns all errors that have occurred from loading since the last call to this method. Since this method
     * "reads" the errors, they are deleted from this class' internal storage when this method is called.
     * Calling this method twice consecutively is guaranteed to return <code>null</code> the second time.
     * @return all errors that have occurred from loading since the last call to this method.
     */
    public String[] readAllErrors() {
        if (errorsPending()) {
            String[] result = errors.toArray(String[]::new);
            errors.clear();
            return result;
        }
        else
            return null;
    }
}
