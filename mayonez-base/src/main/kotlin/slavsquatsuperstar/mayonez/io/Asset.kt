package slavsquatsuperstar.mayonez.io

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Any resource or file used by this program. Stores a path and manages input and output to that location.
 * Each asset must have a constructor with one string, since this is called using reflection by [Assets.createAsset]
 *
 * @author SlavSquatSuperstar
 */
// TODO auto set asset class
open class Asset(val filename: String) {

    // Try to read from classpath first; if not, look to local
    private val type: AssetType = if (Assets.getClasspathURL(filename) != null) AssetType.CLASSPATH else AssetType.EXTERNAL
    private val isClasspath: Boolean = (type == AssetType.CLASSPATH)

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
     * Creates an [OutputStream] to this file that allows data to be saved if the asset is an external file. If the file
     * does not yet exist, then a new file is created.
     *
     * @param append whether to add data to an existing file's contents instead of overwriting it
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
        if (isClasspath) Assets.getClasspathURL(filename)
        else Assets.getFileURL(filename)

    /**
     * Whether this file exists and can be accessed.
     *
     * @return If the file exists. For classpath assets, returns true if a resource exists at this path. For external
     * assets, returns true if a file, and not a directory, currently exists at this path.
     */
    fun isValid(): Boolean {
        return when (type) {
            AssetType.CLASSPATH -> path != null
            AssetType.EXTERNAL -> file.isFile
        }
    }

    override fun toString(): String = "$type ${javaClass.simpleName} \"$filename\""

}