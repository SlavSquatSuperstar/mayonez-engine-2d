package slavsquatsuperstar.fileio

import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import slavsquatsuperstar.mayonez.Logger
import slavsquatsuperstar.mayonez.Logger.trace
import slavsquatsuperstar.mayonez.Logger.warn
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

/**
 * A utility class for file I/O and managing the application's resources.
 *
 * @author SlavSquatSuperstar
 */
object Assets {

    private val ASSETS = HashMap<String, Asset?>()

    init {
        scanAll("assets") // Scan all files inside resources root -> assets folder
    }

    @JvmStatic
    fun scanAll(directory: String) {
        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(directory)).setScanners(ResourcesScanner())
        )
        val assets = reflections.getResources(Pattern.compile(".*\\.*"))
        assets.forEach { createAsset(it, true) } // Create an asset from each path
        ASSETS.forEach { Logger.log(it.value) }
    }

    // Asset Getters / Setters

    @JvmStatic
    fun hasAsset(filename: String): Boolean = filename in ASSETS && ASSETS[filename] != null

    /**
     * Creates a new [Asset] and stores it for future use.
     *
     * @param filename    the location of the asset
     * @param isClasspath whether this asset is a classpath resource and not an external file
     */
    @JvmStatic
    fun createAsset(filename: String, isClasspath: Boolean) {
//        if (!hasAsset(filename)) {
        val asset = Asset(filename, isClasspath)
        if (asset.exists()) {
            ASSETS[filename] = asset
            trace("Assets: Created resource at \"%s\"", filename)
        } else {
            ASSETS[filename] = null
            trace("Assets: Resource at \"%s\" does not exist", filename)
        }
//        } else {
//            trace("Assets: Resource \"%s\" already exists", filename)
//        }
    }

    /**
     * Retrieves the [Asset] at the given location.
     *
     * @param filename the path to the file
     * @param isClasspath whether this asset is a classpath resource and not an external file
     */
    @JvmStatic
    fun getAsset(filename: String, isClasspath: Boolean): Asset? {
        if (!hasAsset(filename))
            createAsset(filename, isClasspath)
        return ASSETS[filename]
    }

    // Asset URL Methods

    /**
     * Accesses a classpath resource from within the .jar executable.
     *
     * @param path The location of the file inside the root resource directory.
     * @return The file's URL.
     */
    @JvmStatic
    fun getClasspathURL(path: String): URL? = ClassLoader.getSystemResource(path)

    /**
     * Accesses an external file from outside the .jar executable.
     *
     * @param filename the location of the file inside the computer's local storage
     * @return the file's URL
     */
    @JvmStatic
    fun getFileURL(filename: String): URL? {
        return try {
            File(filename).toURI().toURL()
        } catch (e: MalformedURLException) {
            warn("Assets: Invalid file path \"%s\"", filename)
            null
        }
    }
}