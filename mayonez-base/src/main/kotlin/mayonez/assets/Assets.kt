package mayonez.assets

import mayonez.*

/**
 * Manages the application's resources and allows users to create and retrieve
 * new [Asset] files.
 *
 * Usage: Upon startup, the program automatically scans the `assets/` folder
 * under `src/main/resources` or inside the .jar and adds all files to the
 * asset pool. The user can scan any classpath or external folders using
 * [Assets.scanDirectory]. All resource paths start inside the jar, while all
 * external paths are relative the folder containing the jar. To create an
 * individual asset, the user may call [Assets.createAsset], and the asset
 * system will first search for a classpath resource, and then an external
 * file.
 *
 * To retrieve a created asset, call [Assets.getAsset]. The user may optionally
 * supply a subclass of [Asset] with [Assets.getAsset], which will initialize
 * that asset as an instance of that class. For example, calling
 * `Assets.getAsset("foo/bar.txt", TextFile.class)` will return a
 * [mayonez.assets.text.TextFile] with the path `foo/bar.txt`.
 *
 * See [Asset] for more details.
 *
 * @author SlavSquatSuperstar
 */
// TODO preload stage, map extensions to subclass
object Assets {

    // Initialization Fields
    private const val ASSETS_ROOT_DIR = "assets"
    private var initialized: Boolean = false
    private var loadedResources: Boolean = false

    // Asset Fields
    private val assets: MutableMap<String, Asset> = HashMap()

    init {
        initialize()
    }

    fun initialize() {
        if (!initialized) {
            // Create the singleton object and the map
            Logger.debug("Current launch directory at ${System.getProperty("user.dir")}")
            initialized = true
        }
    }

    fun loadResources() {
        if (!loadedResources) {
            Logger.debug("Loading program assets...")
            scanDirectory(ASSETS_ROOT_DIR)
            loadedResources = true
        }
    }

    // Search Folder Methods

    /**
     * Recursively adds all the resources inside a jar file or local directory
     * to the asset pool.
     *
     * @param directory a path to a folder
     */
    @JvmStatic
    fun scanDirectory(directory: String) {
        val path = FilePath.of(directory)
        val resources = path.scanFiles()
        resources.forEach { createAsset(it.path) } // Create an asset from each path
        Logger.debug("Scanned ${resources.size} resources inside \"$directory\"")
    }

    // Asset Methods

    /**
     * Indicates whether the [Asset] stored under the given path exists.
     *
     * @param path the location of the asset
     * @return if a file exists at the given path
     */
    @JvmStatic
    fun hasAsset(path: String): Boolean = path.toOS() in assets

    /**
     * Creates a new [Asset] if it does not exist already, and stores it for
     * future use.
     *
     * @param path the location of the asset
     * @return the asset
     */
    @JvmStatic
    fun createAsset(path: String): Asset {
        val osPath = path.toOS()
        if (hasAsset(osPath)) {
            Logger.debug("Asset \"$osPath\" already exists")
        } else {
            assets[osPath] = Asset(osPath)
            Logger.debug("Loaded asset \"$osPath\"")
        }
        return assets[osPath]!!
    }

    /**
     * Instantiates an [Asset] under the given subclass and overwrites it in
     * storage.
     *
     * @param path the location of the asset
     * @param assetClass the subclass of the asset
     * @return the asset as a subclass instance, if successfully created
     */
    @JvmStatic
    fun <T : Asset> createAsset(path: String, assetClass: Class<T>): T? {
        val ctor = assetClass.getDeclaredConstructor(String::class.java)
        val asset = assetClass.cast(ctor.newInstance(path)) ?: return null
        asset.init()
        assets[path] = asset
        Logger.debug("Loaded asset \"%s\" as %s", path, assetClass.simpleName)
        return asset
    }

    // Asset Getters

    /**
     * Retrieves the [Asset] at the given path.
     *
     * @param path the location of the asset
     * @return the asset if it exists, otherwise null
     */
    @JvmStatic
    fun getAsset(path: String): Asset? = assets[path.toOS()]

    /**
     * Retrieves the [Asset] under the specified path and re-instantiates it
     * under the given Asset subclass. If an asset already existed under a
     * different subclass, it will be freed first.
     *
     * @param path the location of the asset
     * @param cls the asset subclass
     * @return a subclass instance with the same path, if the asset is valid
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Asset> getAsset(path: String, cls: Class<T>): T? {
        val asset = getAsset(path) // check if asset exists and is same class
        val notInitialized = (asset == null || !cls.isInstance(asset))
        if (notInitialized) {
            asset?.free()
            return createAsset(path, cls)
        } else {
            return asset as? T
        }
    }

    /** Empties all Assets from the asset pool and frees them. */
    @JvmStatic
    fun clearAssets() {
        assets.values.forEach(Asset::free)
        assets.clear()
        Logger.debug("Cleared all assets")
    }

    override fun toString(): String {
        return "Assets (Size = ${assets.size})"
    }

}

private fun String.toOS() = PathUtil.convertPath(this)