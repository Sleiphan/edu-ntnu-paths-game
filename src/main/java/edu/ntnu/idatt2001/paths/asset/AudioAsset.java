package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

public class AudioAsset implements Asset<MediaPlayer> {

    private MediaPlayer asset = null;
    private final String filePath;
    private boolean loaded;

    public AudioAsset(String filePath) throws IOException {
        this.filePath = filePath;
    }

    public void

    @Override
    public void load() throws IOException {
        String URI = new File(filePath).toURI().toString();
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
