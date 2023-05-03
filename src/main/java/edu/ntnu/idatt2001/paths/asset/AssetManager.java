package edu.ntnu.idatt2001.paths.asset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Effectively allows a developer to control which assets are kept loaded in memory, through a hashmap structure.
 *
 * @param <R> The data type of the asset this class is to manage.
 * @param <T> A class implementing the Asset interface handling a data object of type R.
 */
public class AssetManager<R, T extends Asset<R>> {

    private final List<String> errors = new ArrayList<>();

    private final HashMap<String, T> assets;

    /**
     *
     * @param assets
     */
    public AssetManager(HashMap<String, T> assets) {
        this.assets = assets;
    }

    /**
     * Serves the caller with the asset object (of type R) associated with the submitted key. <br>
     * <br>
     * If the asset is not already loaded into memory, the asset is only temporarily loaded into memory
     * to be returned by this method. In this case, the asset's <code>unload</code>-method is called
     * before returning the asset object. <br>
     * <br>
     * This method returns null both when there was no mapping for the submitted key, or an IOException occurred
     * due to a failed attempt to load an asset. It is recommended to check for any errors with
     * <code>AssetManager::getLastError</code> if this method returns <code>null</code>.
     * @param key
     * @return The asset object associated with the submitted key, or <code>null</code> if either no mapping exists for the key, or an
     * error occurred during asset loading.
     */
    public R getAsset(String key) {
        T asset = assets.get(key);

        if (asset == null)
            return null;

        if (asset.isLoaded())
            return asset.get();

        boolean success = tryLoadAssetOrRegisterError(asset, key);
        R result = success ? asset.get() : null;
        asset.unload(); // Unload the asset again, as it is not supposed to stay loaded in memory.
        return result;
    }

    /**
     * Makes sure to keep an asset loaded in memory, allowing for faster access to the asset.
     * @param key A key mapped to the requested asset.
     * @return True if the asset was successfully loaded into memory, or <code>false</code> if no mapping for the
     * key exists, or an exception occurred and was recorded.
     */
    public boolean loadAsset(String key) {
        T asset = assets.get(key);
        if (asset == null)
            return false;

        return tryLoadAssetOrRegisterError(asset, key);
    }

    public void loadAll() {
        for (Map.Entry<String, T> assetEntry : assets.entrySet())
            if (!assetEntry.getValue().isLoaded())
                tryLoadAssetOrRegisterError(assetEntry.getValue(), assetEntry.getKey());
    }

    /**
     * Unloads an asset from memory, freeing up memory space, but increasing the time it takes for the asset to
     * be accessed through <code>AssetManager::getAsset</code>.
     * @param key A key mapped to the requested asset.
     * @return <code>true</code> if the asset was successfully unloaded from memory, or null if no no mapping for the
     * key exists.
     */
    public boolean unloadAsset(String key) {
        T asset = assets.get(key);
        if (asset == null)
            return false;

        if (asset.isLoaded())
            asset.unload();
        return true;
    }

    public void unloadAll() {
        for (T asset : assets.values())
            asset.unload();
    }

    /**
     * Indicates whether an asset is currently loaded into memory.
     * @param key A key mapped to the requested asset.
     * @return <code>true</code> if the the asset is currently loaded into memory.
     * Otherwise, returns <code>false</code>.
     */
    public boolean isLoaded(String key) {
        T asset = assets.get(key);
        if (asset == null)
            return false;

        return asset.isLoaded();
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
            return errors.remove(0);
        else
            return null;
    }

    /**
     * Tries to load the asset with the path registered at the specified key. If an IOException occurs,
     * it is recorded in this class' error-list. The key MUST be the same key used to register the asset within this class.
     * @param asset
     * @param key The key associated with this asset. Only used for constructing a more comprehensible
     * @return True if the asset was loaded successfully, or false if an error was captured.
     */
    private boolean tryLoadAssetOrRegisterError(T asset, String key) {
        try {
            asset.load();
        } catch (IOException e) {
            errors.add("Error loading asset: " + e.getMessage());
            return false;
        }

        return true;
    }
}
