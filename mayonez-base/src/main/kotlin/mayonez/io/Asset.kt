package mayonez.io

import mayonez.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.pathString

/**
 * Any resource or file used by this program. Stores a path and
 * manages input and output to that location. Each asset must have a
 * constructor with one string, since this is called using reflection by
 * [Assets.createAsset]
 *
 * @author SlavSquatSuperstar
 */
// TODO auto set asset class
open class Asset(filename: String) {

    val filename: String = getOSFilename(filename)

    // Try to read from classpath first; if not, look to local
    private val type: AssetType = getAssetType(this.filename)

    val isClasspath: Boolean = (type == AssetType.CLASSPATH)

    // I/O Methods

    /**
     * Creates an [InputStream] from this file that allows data to be read.
     *
     * @return ths input stream
     */
    @Throws(IOException::class)
    fun inputStream(): InputStream? {
        return if (isClasspath) ClassLoader.getSystemResourceAsStream(filename)
        else Files.newInputStream(Paths.get(filename))
    }

    /**
     * Creates an [OutputStream] to this file that allows data to be saved if
     * the asset is an external file. If the file does not yet exist, then a
     * new file is created.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     */
    @Throws(IOException::class)
    fun outputStream(append: Boolean): OutputStream? {
        return if (isClasspath) throw IOException("Cannot write to classpath resource $filename")
        else if (!append) Files.newOutputStream(Paths.get(filename), StandardOpenOption.CREATE)
        else Files.newOutputStream(Paths.get(filename), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
    }

    // Helper Methods
    val file: File
        get() = File(filename)

    private val path: URL? =
        if (isClasspath) getClasspathURL(filename)
        else getFileURL(filename)

    /**
     * Whether this file exists and can be accessed.
     *
     * @return If the file exists. For classpath assets, returns true if a
     *     resource exists at this path. For external assets, returns true if a
     *     file, and not a directory, currently exists at this path.
     */
    fun isValid(): Boolean {
        return when (type) {
            AssetType.CLASSPATH -> path != null
            AssetType.EXTERNAL -> file.isFile
        }
    }

    override fun toString(): String = "$type ${javaClass.simpleName} \"$filename\""

    // Helper methods
    companion object {

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
         * @param filename the location of the file inside the computer's local
         *     storage
         * @return the file's URL
         */
        @JvmStatic
        fun getFileURL(filename: String): URL? {
            return try {
                File(filename).toURI().toURL()
            } catch (e: MalformedURLException) {
                Logger.error("Invalid file path \"%s\"", filename)
                null
            }
        }

        /**
         * Returns an asset filename with the correct path separator for the user's
         * OS.
         *
         * @param filename a filename
         * @return the filename with the path separator
         */
        @JvmStatic
        fun getOSFilename(filename: String): String {
            return Paths.get(filename).pathString
        }

        /**
         * Returns the [AssetType] of the given asset filename. If no file
         * can be found with that name, the return value will default to
         * [AssetType.EXTERNAL].
         *
         * @param filename a filename
         * @return the file's asset type
         */
        @JvmStatic
        fun getAssetType(filename: String): AssetType {
            return if (getClasspathURL(filename) != null) AssetType.CLASSPATH
            else AssetType.EXTERNAL
        }

    }

}