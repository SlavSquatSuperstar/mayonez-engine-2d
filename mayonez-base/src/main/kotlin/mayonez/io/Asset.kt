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
     * Creates an [InputStream] that allows data to be read from this file. The
     * input stream should be closed after use.
     *
     * @return the input stream
     */
    @Throws(IOException::class)
    fun inputStream(): InputStream {
        if (!filePath.isReadable()) throw IOException("$locationType asset $filename is not readable")
        return when (locationType) {
            LocationType.CLASSPATH -> openClasspathInputStream()
            LocationType.EXTERNAL -> openExternalInputStream()
        }
    }

    @Throws(IOException::class)
    private fun openClasspathInputStream(): InputStream {
        return ClassLoader.getSystemResourceAsStream(filename)
            ?: throw IOException("Classpath resource $filename could not be read")
    }

    @Throws(IOException::class)
    private fun openExternalInputStream(): InputStream {
        try {
            return Files.newInputStream(path)
        } catch (e: Exception) {
            throw IOException("External file $filename could not be read")
        }
    }

    /**
     * Creates an [OutputStream] that allows data to be saved to this file.
     * If the file does not yet exist, then a new file is created. The input
     * stream should be closed after use.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     */
    @Throws(IOException::class)
    fun outputStream(append: Boolean): OutputStream {
        if (!filePath.isWritable()) throw IOException("$locationType asset $filename is not writable")
        return if (append) openOutputStream(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        else openOutputStream(StandardOpenOption.CREATE)
    }

    @Throws(IOException::class)
    private fun openOutputStream(vararg options: StandardOpenOption): OutputStream {
        try {
            return Files.newOutputStream(path, *options)
        } catch (e: Exception) {
            throw IOException("External file $filename could not be saved")
        }
    }


    override fun toString(): String = "$locationType ${javaClass.simpleName} \"$filename\""

}