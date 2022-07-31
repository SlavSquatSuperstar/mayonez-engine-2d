package slavsquatsuperstar.mayonez.io

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import slavsquatsuperstar.mayonez.Logger
import slavsquatsuperstar.mayonez.Mayonez
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Path
import java.util.regex.Pattern

/**
 * The central asset pool for the application and a utility class for file I/O and managing the application's resources safely.
 *
 * @author SlavSquatSuperstar
 */
// TODO preload stage, map filetype to subclass
object Assets {

    private val ASSETS = HashMap<String, Asset>()
    private val ignore: Array<String> = arrayOf("META-INF", "kotlin", "javassist", "macos")

    init {
        if (!Mayonez.INIT_ASSETS) Mayonez.init()
    }

    internal fun getCurrentDirectory() {
        Logger.debug("Assets: Current launch directory at ${File("./").absolutePath}")
    }

    private fun isIgnored(filename: String): Boolean {
        for (str in ignore) if (filename.startsWith(str)) return true
        return false
    }

    /**
     * Recursively searches a resource directory inside the JAR and adds all assets.
     *
     * @param directory a folder inside the jar
     */
    @JvmStatic
    fun scanResources(directory: String) {
        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(directory)).setScanners(Scanners.Resources)
        )
        val resources = reflections.getResources(Pattern.compile(".*\\.*"))
        var numAssets = 0
        resources.forEach {
            if (!isIgnored(it)) {
                createAsset(it)
                numAssets++
            }
        } // Create an asset from each path
        Logger.debug("Assets: Created $numAssets resources inside \"$directory\"")
    }

    /**
     * Recursively searches a file directory outside the JAR and adds all assets.
     *
     * @param directory a folder outside the jar
     */
    @JvmStatic
    fun scanFiles(directory: String) {
        val files = searchFiles(directory)
        files.forEach { createAsset(it) }
        Logger.debug("Assets: Created ${files.size} files inside $directory")
    }

    @JvmStatic
    private fun searchFiles(directory: String): MutableList<String> {
        val folder = File(directory)
        val files: MutableList<String> = ArrayList()
        if (folder.listFiles() == null) return files // If file return empty list

        for (f in folder.listFiles()!!) {
            if (f.name == ".DS_Store") continue
            val filename = Path.of(directory, f.name).toString()
            if (f.isFile) files.add(filename) else {
                files.addAll(searchFiles(filename))
            }
        }
        return files
    }

    // Asset Accessors / Mutators

    /**
     * Indicates whether the [Asset] stored under the given location exists.
     *
     * @param filename the location of the asset
     * @return if a file exists at the given path
     */
    @JvmStatic
    fun hasAsset(filename: String): Boolean = filename in ASSETS

    /**
     * Retrieves the [Asset] at the given location, if it exists.
     *
     * @param filename the path to the file
     */
    @JvmStatic
    fun getAsset(filename: String): Asset? = ASSETS[filename]

    /**
     * Retrieves and replaces the [Asset] under the specified filename as in instance of any Asset subclass.
     *
     * @param filename  the asset location
     * @param cls       the asset subclass
     * @return a subclass instance with the same {@link AssetType}, if the asset is valid.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Asset> getAsset(filename: String, cls: Class<T>): T? {
        val asset = getAsset(filename)
        return if (asset != null && !cls.isInstance(asset))
            createAsset(filename, cls)
        else asset as? T
    }

    /**
     * Creates a new [Asset] if it does not exist already, and stores it for future use.
     *
     * @param filename the location of the asset
     * @return the asset
     */
    @JvmStatic
    fun createAsset(filename: String): Asset {
        if (hasAsset(filename)) {
            Logger.debug("Assets: Resource \"%s\" already exists", filename)
        } else {
            ASSETS[filename] = Asset(filename)
            Logger.debug("Assets: Created resource at \"%s\"", filename)
        }
        return getAsset(filename)!!
    }

    /**
     * Instantiates an [Asset] under the given subclass and overwrites it in storage.
     *
     * @param filename   the location of the asset
     * @param assetClass the subclass of the asset
     * @return the asset as a subclass instance, if successfully created
     */
    @JvmStatic
    fun <T : Asset> createAsset(filename: String, assetClass: Class<T>): T? {
        val ctor = assetClass.getDeclaredConstructor(String::class.java)
        val asset = assetClass.cast(ctor.newInstance(filename)) ?: return null
        ASSETS[filename] = asset
        Logger.debug("Assets: Set resource at \"%s\" as %s", filename, assetClass.simpleName)
        return asset
    }

    /**
     * Empties all Assets from the asset pool.
     */
    @JvmStatic
    fun clearAssets() {
        ASSETS.clear()
    }

    // Asset URL Methods

    /**
     * Accesses a classpath resource from within the .jar executable.
     *
     * @param path the location of the file inside the root resource directory
     * @return the file's URL
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
            Logger.error("Assets: Invalid file path \"%s\"", filename)
            null
        }
    }

    override fun toString(): String {
        return "Assets (${ASSETS.size} ${if (ASSETS.size == 1) "item" else "items"})"
    }

}