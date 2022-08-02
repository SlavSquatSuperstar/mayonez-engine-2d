package slavsquatsuperstar.mayonez.io

import org.reflections.Reflections
import org.reflections.scanners.Scanners
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

    private val assets = HashMap<String, Asset>()

    init {
        if (!Mayonez.INIT_ASSETS) Mayonez.init()
    }

    // doesn't work for jar
    internal fun getCurrentDirectory() {
        Logger.debug("Assets: Current launch directory at ${System.getProperty("user.dir")}")
    }

    // Search Folder Methods

    /**
     * Recursively searches a resource directory inside the JAR and adds all assets.
     *
     * @param directory a folder inside the jar
     */
    /*
     * Sources:
     * - https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
     * - https://stackoverflow.com/questions/10910510/get-a-array-of-class-files-inside-a-package-in-java/30149061
     * - https://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
     * - https://www.logicbig.com/how-to/java/find-classpath-files-under-folder-and-sub-folder.html
     */
    @JvmStatic
    fun scanResources(directory: String) {
        val resources = Reflections(directory, Scanners.Resources).getResources(Pattern.compile(".*\\.*"))
        resources.forEach { createAsset(it) } // Create an asset from each path
        Logger.debug("Assets: Loaded ${resources.size} resources inside \"$directory\"")
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
        Logger.debug("Assets: Loaded ${files.size} files inside $directory")
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

    // Asset Methods

    /**
     * Indicates whether the [Asset] stored under the given location exists.
     *
     * @param filename the location of the asset
     * @return if a file exists at the given path
     */
    @JvmStatic
    fun hasAsset(filename: String): Boolean = filename in assets

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
            assets[filename] = Asset(filename)
            Logger.debug("Assets: Loaded resource at \"%s\"", filename)
        }
        return assets[filename]!!
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
        assets[filename] = asset
        Logger.debug("Assets: Loaded %s at \"%s\"", assetClass.simpleName, filename)
        return asset
    }

    // Asset Getters

    /**
     * Retrieves the [Asset] at the given location.
     *
     * @param filename the path to the file
     * @return the asset, if it exists.
     */
    @JvmStatic
    fun getAsset(filename: String): Asset? = assets[filename]

    /**
     * Retrieves and replaces the [Asset] under the specified filename as in instance of any Asset subclass.
     *
     * @param filename the asset location
     * @param cls      the asset subclass
     * @return a subclass instance with the same {@link AssetType}, if the asset is valid.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Asset> getAsset(filename: String, cls: Class<T>): T? {
        val asset = getAsset(filename) // check if asset exists and is same class
        return if (asset == null || !cls.isInstance(asset)) createAsset(filename, cls)
        else asset as? T
    }

    /**
     * Retrieves the asset at the given location as a [JSONFile].
     *
     * @param filename the path to the file
     * @return the text file, if it exists
     */
    @JvmStatic
    fun getJSONFile(filename: String): JSONFile? = getAsset(filename, JSONFile::class.java)

    /**
     * Retrieves the asset at the given location as a [TextFile].
     *
     * @param filename the path to the file
     * @return the text file, if it exists
     */
    @JvmStatic
    fun getTextFile(filename: String): TextFile? = getAsset(filename, TextFile::class.java)

    /**
     * Retrieves the asset at the given location as a [GLTexture].
     *
     * @param filename the path to the file
     * @return the texture, if it exists
     */
    @JvmStatic
    fun getGLTexture(filename: String): GLTexture? = getAsset(filename, GLTexture::class.java)

    /**
     * Retrieves the asset at the given location as a [JTexture].
     *
     * @param filename the path to the file
     * @return the texture, if it exists
     */
    @JvmStatic
    fun getJTexture(filename: String): JTexture? = getAsset(filename, JTexture::class.java)

    /**
     * Retrieves the asset at the given location as a [Shader].
     *
     * @param filename the path to the file
     * @return the shader, if it exists
     */
    @JvmStatic
    fun getShader(filename: String): Shader? = getAsset(filename, Shader::class.java)

    /**
     * Empties all Assets from the asset pool.
     */
    @JvmStatic
    fun clearAssets() {
        assets.clear()
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
        return "Assets (${assets.size} ${if (assets.size == 1) "item" else "items"})"
    }

}