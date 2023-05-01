package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PathsAssetStore {

    private AssetManager<Image, ImageAsset> imageAssets;
    private AssetManager<MediaPlayer, AudioAsset> audioAssets;

    private List<String> errors = new ArrayList<>();



    public PathsAssetStore(HashMap<String, ImageAsset> imageAssets, HashMap<String, AudioAsset> audioAssets) {
        this.imageAssets = new AssetManager<>(imageAssets);
        this.audioAssets = new AssetManager<>(audioAssets);
    }



    public boolean loadImage(String key) {
        boolean loaded = imageAssets.loadAsset(key);

        String err;
        while ((err = imageAssets.getLastError()) != null)
            errors.add(err);

        return loaded;
    }

    public boolean unloadImage(String key) {
        return imageAssets.unloadAsset(key);
    }

    public boolean loadAudio(String key) {
        boolean loaded = audioAssets.loadAsset(key);

        String err;
        while ((err = audioAssets.getLastError()) != null)
            errors.add(err);

        return loaded;
    }

    public boolean unloadAudio(String key) {
        return audioAssets.unloadAsset(key);
    }

    public Image getImageAsset(String key) {
        Image i = imageAssets.getAsset(key);
        if (i == null) {
            String err;
            while ((err = imageAssets.getLastError()) != null)
                errors.add(err);
        }

        return i;
    }

    public MediaPlayer getAudioAsset(String key) {
        MediaPlayer i = audioAssets.getAsset(key);
        if (i == null) {
            String err;
            while ((err = audioAssets.getLastError()) != null)
                errors.add(err);
        }

        return i;
    }

    /**
     * Indicates whether there are unread error messages in this class from failed IO-operations.
     * @return <code>true</code> if there are unread error messages. Otherwise, returns <code>false</code>.
     */
    public boolean unreadErrors() {
        return !errors.isEmpty();
    }

    /**
     * Returns any error that could have occurred during asset loading. The oldest error is returned first. If there
     * are no errors left, this method returns <code>null</code>. Note: when an error is returned from this method,
     * it is deleted from the internal storage within this class, and the next oldest error will be returned after
     * the next call to this method.
     * @return An error message resulting from a failed attempt to load an asset (oldest first), or null if all errors
     * have been read.
     */
    public String getLastError() {
        if (unreadErrors())
            return null;
        else
            return errors.remove(0);
    }
}
