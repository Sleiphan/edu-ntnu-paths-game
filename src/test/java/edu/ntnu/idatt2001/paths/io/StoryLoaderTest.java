package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.FXInitializer;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoryLoaderTest {

    @Test
    public void story_without_assets() throws FileNotFoundException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/story_without_assets/story.paths";
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



    @BeforeAll
    public void setup() {
        FXInitializer.addRef();
    }

    @AfterAll
    public void exitFX() {
        FXInitializer.release();
    }
}
