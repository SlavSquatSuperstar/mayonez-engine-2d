package slavsquatsuperstar.mayonez.fileio

import slavsquatsuperstar.mayonez.Logger.log
import java.util.HashMap
import java.io.File
import java.net.MalformedURLException
import java.net.URL

/**
 * A utility class for managing the application's resources.
 *
 * @author SlavSquatSuperstar
 */
object Assets {

    private val ASSETS = HashMap<String, Asset>()

    // Asset Getters and Setters

    @JvmStatic
    fun hasAsset(filename: String): Boolean {
        return ASSETS.containsKey(filename)
    }

    /**
     * Creates a new asset and stores it for future use.
     *
     * @param filename    The location of the sprite inside "assets/".
     * @param isClasspath Whether this asset is a classpath resource and not an external file
     */
    @JvmStatic
    fun createAsset(filename: String, isClasspath: Boolean) {
        if (!hasAsset(filename)) {
            ASSETS[filename] = Asset(filename, isClasspath)
            log("Created asset \"%s\"", filename)
        } else {
            log("Asset \"%s\" already exists", filename)
        }
    }

    @JvmStatic
    fun getAsset(filename: String, isClassPath: Boolean): Asset? {
        if (!hasAsset(filename)) createAsset(filename, isClassPath)
        return ASSETS[filename]
    }

    // File URL Methods

    /**
     * Accesses a classpath file from within the .jar executable.
     *
     * @param path The location of the file inside the root resources directory.
     * @return The file's URL.
     */
    @JvmStatic
    fun getClasspathURL(path: String?): URL? {
        return ClassLoader.getSystemResource(path)
    }

    /**
     * Accesses an external file from outside the .jar executable.
     *
     * @param filename The location of the file inside the computer's local storage.
     * @return The file's URL.
     */
    @JvmStatic
    fun getFileURL(filename: String?): URL? {
        try {
            return File(filename!!).toURI().toURL()
        } catch (e: MalformedURLException) {
            log("Assets: Invalid file path \"%s\"", filename)
        }
        return null
    }
}