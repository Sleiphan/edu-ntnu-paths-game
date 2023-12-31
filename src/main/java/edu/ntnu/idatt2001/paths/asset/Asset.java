package edu.ntnu.idatt2001.paths.asset;

import java.io.IOException;

/**
 * Represents an asset of any type. The main function of this interface is to allow for
 * memory management through loading and unloading of assets to and from memory. An asset must
 * have its load-method called before the asset-object will be available for use.
 *
 * @param <T> The asset-object's type. This is the type returned by the get()-method and will
 *            often be the type used for holding this asset loaded in memory.
 */
public interface Asset<T> {

    /**
     * Loads this Asset into memory from the specified 'filePath'. The class
     * implementing this interface is responsible for storing the data and
     * knowing where to load the data from when <code>load</code> is called.
     *
     * @throws IOException If any IOException occurs during asset loading.
     */
    void load() throws IOException;

    /**
     * Removes this asset from memory to save memory space.
     */
    void unload();

    /**
     * Returns this asset as complete object (of type <code>T</code>), or
     * <code>null</code> if the asset has not been loaded into memory.
     *
     * @return This asset as an object of type T.
     */
    T get();

    /**
     * Indicates whether this asset is loaded into memory.
     *
     * @return <code>true</code> if this asset is loaded into memory. Otherwise,
     *         returns <code>false</code>.
     */
    boolean isLoaded();
}
