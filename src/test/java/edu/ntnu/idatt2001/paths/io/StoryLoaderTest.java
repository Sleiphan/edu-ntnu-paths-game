package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class StoryLoaderTest {

    @Test
    public void story_without_assets() throws FileNotFoundException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/story_without_assets/story.paths";
        StoryLoader loader = new StoryLoader(filePath);

        boolean assetsFound = loader.foundAssetStore();
        boolean success = loader.load();
        Story story = loader.getStory();
        PathsAssetStore assetStore = loader.getAssetStore();

        assert !assetsFound;
        assert success;
        assert story != null;
        assert assetStore == null;
    }

    @Test
    public void story_with_assets() throws FileNotFoundException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/story_with_assets/story.paths";
        StoryLoader loader = new StoryLoader(filePath);

        boolean assetsFound = loader.foundAssetStore();
        boolean success = loader.load();
        Story story = loader.getStory();
        PathsAssetStore assetStore = loader.getAssetStore();

        assert assetsFound;
        assert success;
        assert story != null;
        assert assetStore != null;
    }
}
