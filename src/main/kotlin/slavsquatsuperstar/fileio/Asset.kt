package slavsquatsuperstar.fileio

import org.apache.commons.io.FileUtils
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

/**
 * A resource file used by this program. Stores a path and opens streams to that location.
 *
 * @author SlavSquatSuperstar
 */
class Asset(val filename: String, private val type: AssetType) {

    internal val isClasspath: Boolean
        get() = type == AssetType.CLASSPATH

    @JvmField
    val path: URL? =
        if (isClasspath) Assets.getClasspathURL(filename)
        else Assets.getFileURL(filename)

    // I/O Methods

    @Throws(IOException::class)
    fun inputStream(): InputStream? {
        return if (isClasspath) ClassLoader.getSystemResourceAsStream(filename)
        else Files.newInputStream(Paths.get(filename))
    }


    @Throws(IOException::class)
    fun outputStream(append: Boolean): OutputStream? {
        return if (isClasspath) throw UnsupportedOperationException("Cannot write to classpath resource $filename")
        else FileUtils.openOutputStream(toFile(), append)
    }

    // Helper Methods

    private fun toFile(): File = File(path!!.path)

    fun isValid(): Boolean {
        return when (type) {
            AssetType.CLASSPATH -> path != null
            AssetType.LOCAL -> toFile().exists()
        }
    }

    override fun toString(): String = "$type asset \"$filename\""

}