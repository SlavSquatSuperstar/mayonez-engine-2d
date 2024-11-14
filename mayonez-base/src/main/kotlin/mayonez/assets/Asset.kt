package mayonez.assets

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
open class Asset(private val filePath: FilePath) {

    constructor(filename: String) : this(FilePath.fromFilename(filename))

    val filename: String = filePath.filename

    // I/O Methods

    /**
     * Opens the [InputStream] for this asset. The input stream should be closed
     * after use.
     *
     * @return the input stream
     * @throws IOException if the asset cannot be read from
     */
    @Throws(IOException::class)
    protected fun openInputStream(): InputStream {
        return filePath.openInputStream()
    }

    /**
     * Opens the [OutputStream] for this asset. The output stream should be closed
     * after use.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     * @throws IOException if the file cannot be written to
     */
    @Throws(IOException::class)
    protected fun openOutputStream(append: Boolean): OutputStream {
        return filePath.openOutputStream(append)
    }

    /** Frees any resources used by this asset after use. */
    open fun free() {}

    // Helper Methods/Classes

    protected fun getFilenameInQuotes(): String = "\"$filename\""

    override fun toString(): String {
        return "${filePath.typeName} ${javaClass.simpleName} \"$filename\""
    }

}