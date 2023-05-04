package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PathsAssetStoreParserTest {

    private static final String TEST_DATA_DIR = System.getProperty("user.dir") + "/src/test/resources/story_with_assets/";
    private String testData = null;



    @Test
    public void parse_image_and_audio() throws IOException {
        testData = Files.readString(Path.of(TEST_DATA_DIR + "story.pathsassets"), Charset.defaultCharset());

        PathsAssetStoreParser p = new PathsAssetStoreParser();
        PathsAssetStore assetStore = p.parsePathsAssetStore(testData, TEST_DATA_DIR);

        Image img       = assetStore.images().getAsset("Title screen");
        MediaPlayer aud = assetStore.getAudioAsset("Title screen");

        assert img != null;
        assert aud != null;
        assert p.getErrorsFromLastParse() == null;

        Platform.exit();
    }



    @Test
    public void catching_all_exceptions() {
        assertDoesNotThrow(() -> {
            String bullshit_data = "" +
                    "\"Bullshit 1\":\"iwueoqndlcasd\"\n"+
                    "\"Bullshit 2\":\"iwueoqndlcasd.png\"\n"+
                    "\"Bullshit 3\":\"https://this.site.does/not/exists.png\"\n" +
                    "\"Bullshit 4\":\"https://google.com/resouce/dont/exist.png\"\n";

            PathsAssetStoreParser parser = new PathsAssetStoreParser();
            PathsAssetStore store = parser.parsePathsAssetStore(bullshit_data, "C:/");
            String[] errors = parser.getErrorsFromLastParse();

            assert errors.length == 4;

            Platform.exit();
        });
    }


    @BeforeAll
    public void setup() {
        Platform.startup(() -> {});
    }

    @AfterAll
    public void exitFX() {
        Platform.exit();
    }
}
