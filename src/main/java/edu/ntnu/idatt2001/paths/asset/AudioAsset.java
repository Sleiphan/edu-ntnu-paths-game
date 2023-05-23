package edu.ntnu.idatt2001.paths.asset;

import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Represents an audio asset in the asset-framework of this project.
 */
public class AudioAsset implements Asset<MediaPlayer> {

    private final String uri;
    private MediaPlayer asset = null;
    private boolean loaded;

    /**
     * Creates a new instance of AudioAsset with the asset at the specified URI.
     *
     * @param uri The location of the audio asset.
     */
    public AudioAsset(String uri) {
        if (uri == null)
            throw new IllegalArgumentException("Argument uri cannot be null");

        this.uri = uri;
    }

    /**
     * Loads this Asset into memory from the specified 'filePath'.
     */
    @Override
    public void load() {
        if (loaded) return;

        asset = new MediaPlayer(new Media(uri));
        loaded = true;
    }

    @Override
    public void unload() {
        asset = null;
        loaded = false;
    }

    /**
     * Returns this audio asset as a MediaPlayer, or <code>null</code> if this asset is not
     * currently loaded into memory.
     *
     * @return this audio asset as a MediaPlayer, or <code>null</code> if this asset is not
     * currently loaded into memory.
     */
    @Override
    public MediaPlayer get() {
        return asset;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
