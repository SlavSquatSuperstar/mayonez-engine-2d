package slavsquatsuperstar.mayonez.assets;

import slavsquatsuperstar.mayonez.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * A utility class for managing the application's resources.
 *
 * @author SlavSquatSuperstar
 */
public final class Assets {

    private static final HashMap<String, Asset> ASSETS = new HashMap<>();

    private Assets() {}

    // Asset Getters and Setters

    public static boolean hasAsset(String filename) {
        return ASSETS.containsKey(filename);
    }

    /**
     * Creates a new asset and stores it for future use.
     *
     * @param filename    The location of the sprite inside "assets/".
     * @param isClasspath Whether this asset is a classpath resource and not an external file
     */
    public static void createAsset(String filename, boolean isClasspath) {
        if (!hasAsset(filename)) {
            ASSETS.put(filename, new Asset(filename, isClasspath));
            Logger.log("Created asset \"%s\"", filename);
        } else {
            Logger.log("Asset \"%s\" already exists", filename);
        }
    }

    public static Asset getAsset(String filename, boolean isClassPath) {
        if (!hasAsset(filename))
            createAsset(filename, isClassPath);
        return ASSETS.get(filename);
    }

    // File URL Methods

    /**
     * Accesses a classpath file from within the .jar executable.
     *
     * @param path The location of the file inside the root resources directory.
     * @return The file's URL.
     */
    static URL getClasspathURL(String path) {
        return ClassLoader.getSystemResource(path);
    }

    /**
     * Accesses an external file from outside the .jar executable.
     *
     * @param filename The location of the file inside the computer's local storage.
     * @return The file's URL.
     */
    static URL getFileURL(String filename) {
        try {
            return new File(filename).toURI().toURL();
        } catch (MalformedURLException e) {
            Logger.log("Assets: Invalid file path \"%s\"", filename);
        }
        return null;
    }

}
