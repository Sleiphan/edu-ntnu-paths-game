package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class AudioAsset implements Asset<MediaPlayer> {

    private MediaPlayer asset = null;
    private final String filePath;
    private boolean loaded;

    public AudioAsset(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void load() throws IOException {
        asset = new MediaPlayer(new Media(filePath)); // May cause an exception. If so, try this: new MediaPlayer(new Media(new File(filePath).toURI().toString()));
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
