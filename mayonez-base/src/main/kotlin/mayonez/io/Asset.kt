package mayonez.io

import mayonez.*
import java.io.*
import java.nio.file.*

/**
 * A resource or file used by this program. Stores a [mayonez.io.FilePath]
 * and opens input and output streams to that location. Each asset
 * must have a constructor with one string, since this is called using
 * reflection by [Assets.createAsset]
 *
 * @author SlavSquatSuperstar
 */
open class Asset(filename: String) {

    private val filePath: FilePath = FilePath(filename)

    val filename: String = filePath.filename

    val locationType: LocationType = this.filePath.locationType

    // Conversion Methods

    private val path: Path
        get() = Paths.get(filename)

    // I/O Methods

    /**
     * Creates an [InputStream] from this file that allows data to be read.
     *
     * @return ths input stream
     */
    @Throws(IOException::class)
    fun inputStream(): InputStream? {
        if (!filePath.isReadable()) throw IOException("$locationType asset $filename is not readable")
        return when (locationType) {
            LocationType.CLASSPATH -> ClassLoader.getSystemResourceAsStream(filename)
            LocationType.EXTERNAL -> Files.newInputStream(path)
        }
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
        if (!filePath.isWritable()) throw IOException("$locationType asset $filename is not writable")
        return if (append) Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        else Files.newOutputStream(path, StandardOpenOption.CREATE)
    }

    override fun toString(): String = "$locationType ${javaClass.simpleName} \"$filename\""

}