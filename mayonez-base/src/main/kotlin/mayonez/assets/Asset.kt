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
// TODO hold streams, close on free
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
     * @throws IOException if the asset cannot be read from
     */
    @Throws(IOException::class)
    fun openInputStream(): InputStream {
        return filePath.openInputStream()
    }

    /**
     * Creates an [OutputStream] that allows data to be saved to this asset,
     * and creates the file on the computer if it doesn't exist. The output
     * stream should be closed after use.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     * @throws IOException if the file cannot be written to
     */
    @Throws(IOException::class)
    fun openOutputStream(append: Boolean): OutputStream {
        return filePath.openOutputStream(append)
    }

    /** Frees any resources used by this asset after use. */
    open fun free() {}

    // Helper Methods/Classes

    protected fun getFilenameInQuotes(): String = "\"$filename\""

    override fun toString(): String = "$locationType ${javaClass.simpleName} \"$filename\""

}