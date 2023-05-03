package edu.ntnu.idatt2001.paths.asset;

import java.io.IOException;

public interface AssetDriver<T> {

    /**
     * Adds the submitted object to the list of objects currently using this asset.
     * When an object wants to use this asset, it must call this method with 'this' as the argument to keep the asset
     * from unloading itself from memory during usage.
     * @param o A
     */
    void addRef(Object ref);
    void release(Object ref);

    void lockInMemory(Object ref);
    void unlockFromMemory(Object ref);

    /**
     * Loads this Asset into memory from the specified 'filePath'. The class implementing
     * this interface is responsible for storing the data and knowing where to load the data from when
     * <code>load</code> is called.
     * @throws IOException If any IOException occurs during asset loading.
     */
    void load() throws IOException;

    /**
     * Removes this asset from memory to save memory space.
     */
    void unload();

    /**
     * Returns this asset as complete object (of type <code>T</code>), or <code>null</code> if the asset has not been loaded into memory.
     * @return This asset as an object of type T.
     */
    T get();

    /**
     * Indicates whether this asset is loaded into memory.
     * @return <code>true</code>if this asset is loaded into memory. Otherwise, returns <code>false</code>.
     */
    boolean isLoaded();
}
