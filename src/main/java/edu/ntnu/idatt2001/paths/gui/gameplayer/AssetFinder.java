package edu.ntnu.idatt2001.paths.gui.gameplayer;

import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import edu.ntnu.idatt2001.paths.io.PathsParser;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class defines the syntax for connecting assets to different passages.
 * The syntax is based on creating a format for the keys used in an asset store,
 * which in our case is PathsAssetStore.
 * And the assets are defined in a .pathsassets-file in the same directory as
 * the .paths-file. <br>
 * <br>
 */
public class AssetFinder {
    public static final String HEALTH_ICON = "UI: Health icon";
    public static final String GOLD_ICON   = "UI: Gold icon";
    public static final String SCORE_ICON  = "UI: Score icon";
    public static final String ITEM_SLOT   = "UI: Item slot";
    public static final String INTERACTION_AREA = "UI: Interaction area";
    public static final String TEXT_AREA = "UI: Text area";

    public static final String PASSAGE_PLAYER = "Player"; // "Passage:Player":"C:/"
    public static final String PASSAGE_LOOK_AT = "LookAt"; // "Passage:LookAt":"C:/"
    public static final String PASSAGE_BACKGROUND = "Background"; // "Passage:Background":"C:/"
    public static final String ITEM_AREA   = "Item area";

    public static final String LINK_ICON = "Icon"; // "Passage:Link:Background":"C:/"

    public static final String AUDIO = "Audio"; // "Passage:Link:Background":"C:/"

    // The data separator used for layering.
    private static final String SEP = ":";

    // The data separator in a .pathsassets-file.
    private static final String PATHSASSETS_SEP = ":";

    private final PathsAssetStore assetStore;

    public AssetFinder(PathsAssetStore assetStore) {
        this.assetStore = assetStore;
    }

    /**
     * Loads into memory all assets that are always on display.
     */
    public void loadAllGlobalAssets() {
        assetStore.images().loadAsset(HEALTH_ICON);
        assetStore.images().loadAsset(GOLD_ICON);
        assetStore.images().loadAsset(SCORE_ICON);
        assetStore.images().loadAsset(ITEM_SLOT);
        assetStore.images().loadAsset(INTERACTION_AREA);
    }

    /**
     * Unloads all assets that are always on display from memory.
     */
    public void unloadAllGlobalAssets() {
        assetStore.images().unloadAsset(HEALTH_ICON);
        assetStore.images().unloadAsset(GOLD_ICON);
        assetStore.images().unloadAsset(SCORE_ICON);
        assetStore.images().unloadAsset(ITEM_SLOT);
        assetStore.images().unloadAsset(INTERACTION_AREA);
    }

    /**
     * Generates a template for specifying the global assets of a story. These
     * are the assets that are constant throughout the story.
     * @return A string containing the template for specifying the global assets
     * of a story.
     */
    public static String generateGlobalTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"" + HEALTH_ICON      + "\"").append(PATHSASSETS_SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"" + GOLD_ICON        + "\"").append(PATHSASSETS_SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"" + SCORE_ICON       + "\"").append(PATHSASSETS_SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"" + ITEM_SLOT        + "\"").append(PATHSASSETS_SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"" + INTERACTION_AREA + "\"").append(PATHSASSETS_SEP).append("\"put_URI_here\"").append("\n");

        return sb.toString();
    }

    /**
     * Generates a template for specifying the assets of a single passage in a
     * story.
     * @param passageTitle The title of the passage.
     * @param linkTitles An array of Strings containing the titles of the
     *                   passage's links.
     * @return
     */
    public static String generatePassageTemplate(String passageTitle, String[] linkTitles) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(passageTitle).append(SEP).append(PASSAGE_PLAYER)    .append("\"").append(SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"").append(passageTitle).append(SEP).append(PASSAGE_LOOK_AT)   .append("\"").append(SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"").append(passageTitle).append(SEP).append(PASSAGE_BACKGROUND).append("\"").append(SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"").append(passageTitle).append(SEP).append(ITEM_AREA)         .append("\"").append(SEP).append("\"put_URI_here\"").append("\n");
        sb.append("\"").append(passageTitle).append(SEP).append(AUDIO)             .append("\"").append(SEP).append("\"put_URI_here\"").append("\n");

        for (String s : linkTitles)
            sb.append("\"").append(passageTitle).append(SEP).append(s).append(SEP).append(LINK_ICON).append("\"").append(SEP).append("\"put_URI_here\"").append("\n");

        return sb.toString();
    }

    public Image getHealthIcon() {
        return assetStore.images().getAsset(HEALTH_ICON);
    }

    public Image getGoldIcon() {
        return assetStore.images().getAsset(GOLD_ICON);
    }

    public Image getScoreIcon() {
        return assetStore.images().getAsset(SCORE_ICON);
    }

    public Image getItemSlot() {
        return assetStore.images().getAsset(ITEM_SLOT);
    }

    public Image getInteractionArea() {
        return assetStore.images().getAsset(INTERACTION_AREA);
    }

    public Image getItemArea(String passageTitle) {
        return assetStore.images().getAsset(passageTitle + SEP + ITEM_AREA);
    }

    public Image getPlayer(String passageTitle) {
        return assetStore.images().getAsset(passageTitle + SEP + PASSAGE_PLAYER);
    }

    public Image getLookAt(String passageTitle) {
        return assetStore.images().getAsset(passageTitle + SEP + PASSAGE_LOOK_AT);
    }

    /**
     * Returns the background image of a passage.
     * @param passageTitle The title of the passage from which to retrieve the asset.
     * @return the background image of a passage.
     */
    public Image getBackground(String passageTitle) {
        return assetStore.images().getAsset(passageTitle + SEP + PASSAGE_BACKGROUND);
    }

    /**
     * Returns the icon associated with the specified link.
     * @param passageTitle The title of the passage from which to retrieve the asset.
     * @param linkTitle The title of the link from which to retrieve the asset.
     * @return the icon associated with the specified link.
     */
    public Image getLinkIcon(String passageTitle, String linkTitle) {
        return assetStore.images().getAsset(passageTitle + SEP + linkTitle + SEP + LINK_ICON);
    }

    /**
     * Returns the asset to be used as the background of the text area.
     * @return the asset to be used as the background of the text area.
     */
    public Image getTextArea() {
        return assetStore.images().getAsset(TEXT_AREA);
    }

    /**
     * Returns the audio to be played at a specific passage.
     * @param passageTitle The title of the passage from which to retrieve the asset.
     * @return the audio to be played at a specific passage.
     */
    public MediaPlayer getAudio(String passageTitle){
        return assetStore.audio().getAsset(passageTitle + SEP + AUDIO);
    }

    public static void generateAssetTemplate(File file) {
        String storyPath = file.getAbsolutePath();
        String fileName = file.getName();
        String targetFile = fileName.replace(".paths",".pathsassets");
        String targetLocation = file.getAbsolutePath().replace(fileName,targetFile);

        PathsParser parser = new PathsParser();
        StringBuilder sb = new StringBuilder();
        sb.append(AssetFinder.generateGlobalTemplate());

        try {
            Story s = parser.fromPathsFormatStory(Files.readString(Path.of(storyPath), StandardCharsets.UTF_8));

            for (Passage p : s.getPassages()) {
                String[] links = p.getLinks().stream().map(Link::getText).toArray(String[]::new);
                sb.append(AssetFinder.generatePassageTemplate(p.getTitle(), links));
            }

            Files.writeString(Path.of(targetLocation), sb.toString(), StandardCharsets.UTF_8);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
