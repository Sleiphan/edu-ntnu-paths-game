package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.asset.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The purpose of this class is to create a PathsAssetStore by reading a .pathsassets-file and the contents of
 * its directory. <br>
 * <br>
 *
 * This class is mainly focused around two methods:
 * <code>parsePathsAssetStore</code> and <code>getErrorsFromLastParse</code>. The former is the actual parser method.
 * The latter returns a collection of errors that caused the last call to <code>parsePathsAssetStore</code> to fail.
 * Note that every time <code>parsePathsAssetStore</code> is called, the errors from the parsing-operation before
 * will be lost.
 */
public class PathsAssetStoreParser {

    public static final String[] IMAGE_FILE_ENDINGS = {".bmp", ".gif", ".jpg", ".png"};
    public static final String[] AUDIO_FILE_ENDINGS = {".mp3"};
    public static final String SUPPORTED_FILE_ENDINGS = ".bmp, .gif, .jpg, .png, .mp3";
    public static final String DATA_SEPARATOR = ":";

    private List<String> loadErrors = new ArrayList<>();


    /**
     * Indicates whether any errors occurred during the last parse-operation.
     * @return <code>true</code> if any errors occurred, or <code>false</code> if no errors occurred.
     */
    public boolean errorsOccurredLastParse() {
        return !loadErrors.isEmpty();
    }

    /**
     * Returns a collection of the errors that occurred during the last call to <code>parsePathsAssetStore</code>.
     * @return The errors from the last parse, or <code>null</code> if no errors occurred.
     */
    public String[] getErrorsFromLastParse() {
        if (errorsOccurredLastParse())
            return loadErrors.toArray(String[]::new);
        else
            return null;
    }


    /**
     * Attempts to collect all assets specified in <code>assetRegisterData</code> from the directory
     * <code>assetDirectoryPath</code>, and stores them as Asset-objects in a PathsAssetStore.
     * @param assetRegisterData Textual data in line-separated key-value format, telling us the key and URI/filepath
     *                          of each asset we expect to find within the specified <code>assetDirectoryPath</code>.
     * @param assetDirectoryPath The directory path where we expect to find the assets.
     * @return A PathsAssetStore containing the assets found in the parsing process.
     */
    public PathsAssetStore parsePathsAssetStore(String assetRegisterData, String assetDirectoryPath) {
        loadErrors = new ArrayList<>();
        assetDirectoryPath = assetDirectoryPath.replaceAll("\\\\", "/");

        HashMap<String, ImageAsset> imageAssets = new HashMap<>();
        HashMap<String, AudioAsset> audioAssets = new HashMap<>();

        String[] lines = assetRegisterData.lines().toArray(String[]::new);
        for (int i = 0; i < lines.length; i++) {
            String[] fields = lines[i].split(DATA_SEPARATOR + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String key  = fields[0].substring(1, fields[0].length() - 1);
            String path = fields[1].substring(1, fields[1].length() - 1);


            path = path.replaceAll("\\\\", "/");

            if (!path.contains("://")) {
                if (path.startsWith("/") || assetDirectoryPath.endsWith("/"))
                    path = assetDirectoryPath + path;
                else
                    path = assetDirectoryPath + "/" + path;
                path = "file:/" + path;
            }


            if (isImageAsset(path)) {
                ImageAsset asset = parseImageAsset(path, i, key);
                if (asset != null)
                    imageAssets.put(key, asset);
            }

            else if (isAudioAsset(path)) {
                AudioAsset asset = parseAudioAsset(path, i);
                if (asset != null)
                    audioAssets.put(key, asset);
            }

            else
                loadErrors.add("Unsupported file type of asset named " + key + " at line " + i + ": \"" + path + "\". Supported types are: " + SUPPORTED_FILE_ENDINGS);
        }

        return new PathsAssetStore(imageAssets, audioAssets);
    }

    private static boolean isImageAsset(String path) {
        for (String s : IMAGE_FILE_ENDINGS)
            if (path.endsWith(s))
                return true;
        return false;
    }

    private static boolean isAudioAsset(String path) {
        for (String s : AUDIO_FILE_ENDINGS)
            if (path.endsWith(s))
                return true;
        return false;
    }

    private ImageAsset parseImageAsset(String URI, int line, String key) {
        ImageAsset asset = null;
        try {
            asset = new ImageAsset(URI);
        } catch (FileNotFoundException e) {
            loadErrors.add("File not found for asset named " + key + " at line " + line + ". URI: " + URI);
        } catch (IllegalArgumentException e) {
            loadErrors.add("Invalid or unsupported asset named " + key + " at line " + line + ". URI: " + URI);
        } catch (UnknownHostException e) {
            loadErrors.add("Could not resolve host when loading asset named " + key + " at line " + line + ". URI: " + URI);
        } catch (Exception e) {
            loadErrors.add("Unknown error when loading asset named " + key + " at line " + line + ". URI: " + URI + ". Error: " + e.getMessage());
        }

        return asset; // If the asset was successfully created, return the asset. Or return null of it could not be created.
    }

    private AudioAsset parseAudioAsset(String URI, int line) {
        AudioAsset asset = null;
        try {
            asset = new AudioAsset(URI);
        } catch (IOException e) {
            loadErrors.add("Could not load asset defined at line " + line + ". URI: " + URI + ". Error: " + e.getMessage());
        }

        return asset;
    }
}
