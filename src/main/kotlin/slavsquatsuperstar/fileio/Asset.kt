package slavsquatsuperstar.fileio

import org.apache.commons.io.FileUtils
import java.io.*
import java.net.URL

/**
 * A resource file used by this program. Stores a path and opens streams to that location.
 *
 * @author SlavSquatSuperstar
 */
open class Asset(val filename: String, private val type: AssetType) {

    private val isClasspath: Boolean
        get() = type == AssetType.CLASSPATH

    @JvmField
    val path: URL? =
        if (isClasspath) Assets.getClasspathURL(filename)
        else Assets.getFileURL(filename)

    // I/O Methods

    @Throws(IOException::class)
    fun inputStream(): InputStream? {
        return if (!isValid()) throw FileNotFoundException("Asset $filename not found")
        else path!!.openStream()
    }


    @Throws(IOException::class)
    fun outputStream(append: Boolean): OutputStream? {
        return when {
            isClasspath -> throw UnsupportedOperationException("Cannot write to classpath resourc $filename")
            !isValid() -> throw FileNotFoundException("Asset $filename not found")
            else -> FileUtils.openOutputStream(toFile(), append)
        }
    }

    // Helper Methods

    private fun toFile(): File = File(path!!.path)

    fun isValid(): Boolean {
        return when (type) {
            AssetType.CLASSPATH -> path != null
            AssetType.LOCAL -> toFile().exists()
            AssetType.OUTPUT -> true
        }
    }

    override fun toString(): String = "$type asset $filename\""

}