package mayonez.io

import mayonez.*
import mayonez.io.image.*
import mayonez.io.text.*
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * The central asset pool for the application and a utility class for file
 * I/O and managing the application's resources safely.
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
        Logger.debug("Current launch directory at ${System.getProperty("user.dir")}")
    }

    // Search Folder Methods

    /**
     * Recursively searches a resource directory inside the JAR and adds all
     * assets.
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
    fun scanResources(directory: String): MutableList<String> {
        val resources = Reflections(directory, Scanners.Resources).getResources(Pattern.compile(".*\\.*"))
//        val resources = searchDirectory(directory, AssetType.CLASSPATH)
        resources.forEach { createAsset(it) } // Create an asset from each path
        Logger.debug("Loaded ${resources.size} resources inside \"$directory\"")
        return ArrayList(resources)
    }

    /**
     * Recursively searches a file directory outside the JAR and adds all
     * assets.
     *
     * @param directory a folder outside the jar
     */
    @JvmStatic
    fun scanFiles(directory: String): MutableList<String> {
        val files = searchDirectory(directory, AssetType.EXTERNAL)
        files.forEach { createAsset(it) }
        Logger.debug("Loaded ${files.size} files inside $directory")
        return files
    }

    private fun searchDirectory(directory: String, assetType: AssetType): MutableList<String> {
        // Files.walk() is recursive, file.listFiles() is not
        return try {
            val pathname = if (assetType == AssetType.CLASSPATH) {
                ClassLoader.getSystemResource(directory).path
            } else {
                if (!File(directory).isDirectory) return ArrayList() // If file return empty list
                directory
            }
            val path = Paths.get(pathname)
            ArrayList(Files.walk(path).filter { file: Path -> Files.isRegularFile(file) }
                .map { p: Path -> p.toAssetName(directory, assetType) }.toList())
        } catch (e: Exception) {
            ArrayList()
        }
    }

    private fun Path.toAssetName(root: String, assetType: AssetType): String {
        val fullPath = this.toString()
        return if (assetType == AssetType.CLASSPATH) {
            val index = fullPath.lastIndexOf(root)
            fullPath.substring(index) // cut of path before root
        } else {
            fullPath
        }
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
     * Creates a new [Asset] if it does not exist already, and stores it for
     * future use.
     *
     * @param filename the location of the asset
     * @return the asset
     */
    @JvmStatic
    fun createAsset(filename: String): Asset {
        if (hasAsset(filename)) {
            Logger.debug("Resource \"%s\" already exists", filename)
        } else {
            assets[filename] = Asset(filename)
            Logger.debug("Loaded resource at \"%s\"", filename)
        }
        return assets[filename]!!
    }

    /**
     * Instantiates an [Asset] under the given subclass and overwrites it in
     * storage.
     *
     * @param filename the location of the asset
     * @param assetClass the subclass of the asset
     * @return the asset as a subclass instance, if successfully created
     */
    @JvmStatic
    fun <T : Asset> createAsset(filename: String, assetClass: Class<T>): T? {
        val ctor = assetClass.getDeclaredConstructor(String::class.java)
        val asset = assetClass.cast(ctor.newInstance(filename)) ?: return null
        assets[filename] = asset
        Logger.debug("Loaded %s at \"%s\"", assetClass.simpleName, filename)
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
     * Retrieves and replaces the [Asset] under the specified filename as in
     * instance of any Asset subclass.
     *
     * @param filename the asset location
     * @param cls the asset subclass
     * @return a subclass instance with the same [AssetType], if the asset is
     *     valid.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Asset> getAsset(filename: String, cls: Class<T>): T? {
        val asset = getAsset(filename) // check if asset exists and is same class
        return if (asset == null || !cls.isInstance(asset)) createAsset(filename, cls)
        else asset as? T
    }

    /**
     * Retrieves the asset at the given location as a [CSVFile].
     *
     * @param filename the path to the file
     * @return the CSV file, if it exists
     */
    @JvmStatic
    fun getCSVFile(filename: String): CSVFile? = getAsset(filename, CSVFile::class.java)

    /**
     * Retrieves the asset at the given location as a [JSONFile].
     *
     * @param filename the path to the file
     * @return the JSON file, if it exists
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
     * Retrieves the asset at the given location as a [Texture].
     *
     * @param filename the path to the file
     * @return the texture, if it exists
     */
    @JvmStatic
    fun getTexture(filename: String): Texture? {
        return if (Mayonez.useGL) getAsset(filename, GLTexture::class.java)
        else getAsset(filename, JTexture::class.java)
    }

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

    /** Empties all Assets from the asset pool. */
    @JvmStatic
    fun clearAssets() {
        assets.clear()
        Logger.debug("Cleared all program resources")
    }

    override fun toString(): String {
        return "Assets (${assets.size} ${if (assets.size == 1) "item" else "items"})"
    }

}