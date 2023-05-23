package edu.ntnu.idatt2001.paths.asset;

import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;


/**
 * Represents the collection of AssetManagers tailored for the Paths-project.
 * It contains one manager for each asset type, namely image- and audio-assets.
 */
public class PathsAssetStore {

    private final AssetManager<Image, ImageAsset> imageAssets;
    private final AssetManager<MediaPlayer, AudioAsset> audioAssets;

    /**
     * Creates a new instance of PathsAssetStore with the specified image and audio assets.
     *
     * @param imageAssets A hashmap containing the images for this asset store.
     * @param audioAssets A hashmap containing the audio assets for this asset store.
     */
    public PathsAssetStore(HashMap<String, ImageAsset> imageAssets,
                           HashMap<String, AudioAsset> audioAssets) {
        this.imageAssets = new AssetManager<>(imageAssets);
        this.audioAssets = new AssetManager<>(audioAssets);
    }

    /**
     * Returns the AssetManager for the images in this asset store.
     *
     * @return the AssetManager for the images in this asset store.
     */
    public AssetManager<Image, ImageAsset> images() {
        return imageAssets;
    }

    /**
     * Returns the AssetManager for the audio assets in this asset store.
     *
     * @return the AssetManager for the audio assets in this asset store.
     */
    public AssetManager<MediaPlayer, AudioAsset> audio() {
        return audioAssets;
    }

    /**
     * Unloads all assets of this asset store.
     */
    public void unloadAll() {
        imageAssets.unloadAll();
        audioAssets.unloadAll();
    }
}
