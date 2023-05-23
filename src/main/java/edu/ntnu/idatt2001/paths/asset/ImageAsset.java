package edu.ntnu.idatt2001.paths.asset;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 * Represents an Image in the asset-framework of this project.
 */
public class ImageAsset implements Asset<Image> {

    private final String URI;
    private Image asset;

    /**
     * Creates a new ImageAsset.
     * @param URI A URI specifying the location of an image.
     * @throws Exception If an exception occurred during the loading of the image.
     */
    public ImageAsset(String URI) throws Exception {
        this.URI = URI;

        Exception err = verify();
        if (err != null)
            throw err;
    }

    /**
     * Tries to load the image into memory to verify the availability of the resource.
     * @return The exception that occurred during the loading of the image.
     */
    private Exception verify() {
        Image asset;
        try {
            asset = new Image(URI, false);
        } catch (NullPointerException | IllegalArgumentException e) {
            return e;
        }

        if (asset.isError())
            return asset.getException();
        else
            return null;
    }

    /**
     * Loads this asset into memory.
     * @throws IOException If an exception occurred during the loading of this asset.
     */
    @Override
    public void load() throws IOException {
        if (isLoaded())
            return;

        asset = new Image(URI, false);

        Exception err = asset.getException();
        if (err != null)
            throw new IOException(err);
    }

    /**
     * Unloads this asset from memory, freeing up memory space.
     */
    @Override
    public void unload() {
        if (!isLoaded())
            return;

        asset.cancel();
        asset = null;
    }

    /**
     * Returns this asset as an Image, or null if the asset has not been loaded into memory.
     * @return this asset as an Image, or null if the asset has not been loaded into memory.
     */
    @Override
    public Image get() {
        return asset;
    }

    /**
     * Indicates whether this asset is currently loaded into memory.
     * @return <code>true</code> if this asset is currently loaded into memory.
     *         Otherwise, returns false.
     */
    @Override
    public boolean isLoaded() {
        return asset != null;
    }
}
