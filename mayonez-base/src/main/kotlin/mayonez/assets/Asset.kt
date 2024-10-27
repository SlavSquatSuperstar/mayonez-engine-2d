package mayonez.assets

import mayonez.io.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * A resource or file used by this program. Stores a [FilePath] and opens
 * input and output streams to that location. Each asset must have a
 * constructor with one string, since this is called using reflection by
 * [Assets.createAsset]
 *
 * @author SlavSquatSuperstar
 */
open class Asset(filename: String) {

    private val filePath: FilePath = FilePath(filename)
    val filename: String = filePath.filename
    val locationType: LocationType = filePath.locationType

    // I/O Methods

    /**
     * Creates an [InputStream] that allows data to be read from this asset.
     * The input stream should be closed after use.
     *
     * @return the input stream
     */
    @Throws(IOException::class)
    fun openInputStream(): InputStream {
        if (!filePath.isReadable()) {
            throw IOException("$locationType asset $filename is not readable")
        }
        return locationType.openInputStream(filename)
    }

    /**
     * Creates an [OutputStream] that allows data to be saved to this asset,
     * and creates the file on the computer if it doesn't exist. The output
     * stream should be closed after use.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     */
    @Throws(IOException::class)
    fun openOutputStream(append: Boolean): OutputStream {
        if (!filePath.isWritable()) {
            throw IOException("$locationType asset $filename is not writable")
        }
        return locationType.openOutputStream(filename, append)
    }

    /** Frees any resources used by this asset after use. */
    open fun free() {}

    // Helper Methods/Classes

    protected fun getFilenameInQuotes(): String = "\"$filename\""

    override fun toString(): String = "$locationType ${javaClass.simpleName} \"$filename\""

}