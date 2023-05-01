package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.image.Image;

import java.io.IOException;

public class ImageAsset implements Asset<Image> {

    private final String filePath;
    private Image asset;
    private boolean loaded;


    public ImageAsset(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void load() throws IOException {
        if (loaded)
            unload();

        asset = new Image(filePath);
        loaded = true;
    }

    @Override
    public void unload() {
        asset.cancel();
        asset = null;
        loaded = false;
    }

    @Override
    public Image get() {
        return asset;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
