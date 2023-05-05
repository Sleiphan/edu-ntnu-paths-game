package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

public class AudioAsset implements Asset<MediaPlayer> {

    private MediaPlayer asset = null;
    private final String URI;
    private boolean loaded;

    public AudioAsset(String URI) throws IOException {
        this.URI = URI;
    }

    @Override
    public void load() throws IOException {
        asset = new MediaPlayer(new Media(URI));
        loaded = true;
    }

    @Override
    public void unload() {
        if (loaded)
            asset = null;
        loaded = false;
    }

    @Override
    public MediaPlayer get() {
        return asset;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
