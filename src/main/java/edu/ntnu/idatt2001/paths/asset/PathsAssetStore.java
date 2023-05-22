package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
