package slavsquatsuperstar.fileio

import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
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

    // TODO reload, create if absent

    private val ASSETS = HashMap<String, Asset?>()

    @JvmStatic
    fun scanAllResources(directory: String) {
        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(directory)).setScanners(ResourcesScanner())
        )
        val assets = reflections.getResources(Pattern.compile(".*\\.*"))
        assets.forEach { createAsset(it, AssetType.CLASSPATH) } // Create an asset from each path
    }

    // Asset Accessors / Mutators

    /**
     * Indicates whether the [Asset] stored under the given location exists.
     *
     * @param filename    the location of the asset
     * @return if a file exists at the given path
     */
    @JvmStatic
    fun hasAsset(filename: String): Boolean = filename in ASSETS && ASSETS[filename] != null

    /**
     * Creates a new [Asset] and stores it for future use.
     *
     * @param filename    the location of the asset
     * @param type whether this asset is a classpath resource and not an external file
     * @return if a file was successfully created
     */
    @JvmStatic
    fun createAsset(filename: String, type: AssetType): Boolean {
        return if (!hasAsset(filename)) {
            val asset = Asset(filename, type)
            if (asset.isValid()) {
                ASSETS[filename] = asset
                trace("Assets: Created resource at \"%s\"", filename)
                true
            } else {
                ASSETS[filename] = null
                trace("Assets: Resource at \"%s\" does not exist", filename)
                false
            }
        } else {
            trace("Assets: Resource \"%s\" already exists", filename)
            false
        }
    }

    /**
     * Retrieves the [Asset] at the given location.
     *
     * @param filename the path to the file
     * @param type
     */
    @JvmStatic
    fun getAsset(filename: String, type: AssetType): Asset? {
        if (!hasAsset(filename))
            createAsset(filename, type)
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

    // General File Methods

//    fun openInputStream(filename: String, isClasspath: Boolean): InputStream {
//        val inputStream = if (isClasspath) ClassLoader.getSystemResourceAsStream(filename)
//        else Files.newInputStream(Paths.get(filename))
//        // The stream holding the file content
//        return inputStream ?: throw IllegalArgumentException("file not found: $filename")
//    }
//
//    fun getContents(filename: String, isClasspath: Boolean): ByteArray? {
//        return openInputStream(filename, isClasspath).readAllBytes() ?: null
//    }

}