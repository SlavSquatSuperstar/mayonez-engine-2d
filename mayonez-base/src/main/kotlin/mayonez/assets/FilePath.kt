package mayonez.assets

import java.io.*
import java.net.URL
import java.util.Objects

/**
 * Represents the location of a resource on the computer's file system or
 * inside the current .jar file and facilitates read and write operations.
 *
 * @author SlavSquatSuperstar
 */
abstract class FilePath(
    /** The string representation of this path. */
    val filename: String
) {

    companion object {
        /**
         * Creates a FilePath and automatically determines the location type.
         * Returns a classpath resource if one exists at this path, or otherwise
         * defaults to an external file.
         *
         * @param filename the path filename
         */
        @JvmStatic
        fun fromFilename(filename: String): FilePath {
            val classpathFilePath = ClasspathFilePath(filename)
            return if (classpathFilePath.exists()) classpathFilePath
            else ExternalFilePath(filename)
        }
    }

    // Path Methods

    /**
     * Whether there currently exists a file or directory at this path.
     *
     * @return if this path is valid
     */
    abstract fun exists(): Boolean

    /**
     * Whether there exists a normal file at this path that can be read from,
     * assuming file system permissions allow.
     *
     * @return if this path has a readable file
     */
    abstract fun isReadable(): Boolean

    /**
     * Whether there exists a normal file at this path that can be written to,
     * assuming file system permissions allow.
     *
     * @return if this path has a writable file
     */
    abstract fun isWritable(): Boolean

    protected fun assertReadable() {
        if (!isReadable()) throw IOException("$this is not readable")
    }

    protected fun assertWritable() {
        if (!isWritable()) throw IOException("$this is not writable")
    }

    // Stream Methods

    /**
     * Open an [InputStream] that allows data to be read from the file at this
     * path. The input stream should be closed after use.
     *
     * @return the input stream
     * @throws IOException if the file cannot be read from
     */
    @Throws(IOException::class)
    abstract fun openInputStream(): InputStream

    /**
     * Open an [OutputStream] that allows data to be saved to the file at this
     * path. If the file does not exist, then it will be created. The output
     * stream should be closed after use.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     * @throws IOException if the file cannot be written to
     */
    @Throws(IOException::class)
    abstract fun openOutputStream(append: Boolean): OutputStream

    // Conversion Methods

    /**
     * Get the file represented by this path, which may or may not exist.
     *
     * @return the file
     */
    abstract fun getFile(): File

    /**
     * Get the URL represented by this path, which is non-null if the path
     * exists.
     *
     * @return the path's URL, or null
     */
    abstract fun getURL(): URL?

    internal abstract val typeName: String

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return other is FilePath && other.filename == this.filename
    }

    override fun hashCode(): Int {
        return Objects.hash(filename, typeName)
    }

    override fun toString(): String = "${javaClass.simpleName} $filename"

}