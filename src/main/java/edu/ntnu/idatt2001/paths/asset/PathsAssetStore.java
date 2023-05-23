package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Represents the collection of AssetManagers tailored for the Paths-project.
 * It contains one manager for each asset type, namely image- and audio-assets.
 */
public class PathsAssetStore {

    private AssetManager<Image, ImageAsset> imageAssets;
    private AssetManager<MediaPlayer, AudioAsset> audioAssets;



    public PathsAssetStore(HashMap<String, ImageAsset> imageAssets, HashMap<String, AudioAsset> audioAssets) {
        this.imageAssets = new AssetManager<>(imageAssets);
        this.audioAssets = new AssetManager<>(audioAssets);
    }



    public AssetManager<Image, ImageAsset> images() {
        return imageAssets;
    }

    public AssetManager<MediaPlayer, AudioAsset> audio() {
        return audioAssets;
    }

    public void unloadAll() {
        imageAssets.unloadAll();
        audioAssets.unloadAll();
    }
}
