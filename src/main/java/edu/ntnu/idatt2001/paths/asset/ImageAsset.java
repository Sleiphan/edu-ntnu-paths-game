package edu.ntnu.idatt2001.paths.asset;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageAsset implements Asset<Image> {

    private final String URI;
    private Image asset;
    private boolean loaded;


    public ImageAsset(String URI) throws Exception {
        this.URI = URI;

        Exception err = verify();
        if (err != null)
            throw err;
    }

    private Exception verify() {
        Image asset;
        try {
            asset = new Image(URI, false);
        } catch (Exception e) {
            return e;
        }

        if (asset.isError())
            return asset.getException();
        else
            return null;
    }

    @Override
    public void load() throws IOException {
        if (loaded)
            return;

        asset = new Image(URI, true);

        Exception err = asset.getException();
        if (err != null)
            throw new IOException(err);

        loaded = true;
    }

    @Override
    public void unload() {
        if (!loaded)
            return;

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
