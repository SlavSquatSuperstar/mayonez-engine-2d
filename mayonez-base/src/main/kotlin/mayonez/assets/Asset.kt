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
open class Asset(
    /** The file path that represents this asset's location. */
    protected val filePath: FilePath
) {

    constructor(path: String) : this(FilePath.of(path))

    /** This asset's path string. */
    val path: String = filePath.path

    /** This asset's filename. */
    val filename: String
        get() = filePath.filename

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

    /**
     * Allocates any resources required by this asset after creation.
     * Automatically called through [Assets.createAsset].
     */
    open fun init() {}

    /**
     * Frees any resources used by this asset after use. Automatically called
     * through [Assets.clearAssets].
     */
    open fun free() {}

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return other is Asset && other.path == this.path
    }

    override fun hashCode(): Int = path.hashCode()

    override fun toString(): String = "${javaClass.simpleName} $path"

}