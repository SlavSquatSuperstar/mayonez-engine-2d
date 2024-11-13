package mayonez.assets

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/**
 * Represents the location of an [mayonez.assets.Asset] on the computer's
 * file system and describes whether it is readable or writable.
 *
 * @author SlavSquatSuperstar
 */
abstract class FilePath2(val filename: String) {

    companion object {
        /**
         * Creates a FilePath and automatically determines the location type.
         * Returns a classpath resource if one exists at this path, or otherwise
         * defaults to an external file.
         *
         * @param filename the asset filename
         */
        @JvmStatic
        fun fromFilename(filename: String): FilePath2 {
            val classpathFilePath = ClasspathFilePath(filename)
            return if (classpathFilePath.exists()) classpathFilePath
            else ExternalFilePath(filename)
        }
    }

    // File Status Methods

    /**
     * Whether a file or directory that currently exists at this path.
     *
     * @return if this path leads to a valid file
     */
    abstract fun exists(): Boolean

    /**
     * Whether there is a file at this path  can be read from.
     *
     * @return if this path has a readable file
     */
    abstract fun isReadable(): Boolean

    // Stream Methods

    /**
     * Whether there is a file at this path that can be written to.
     *
     * @return if this path has a writable file
     */
    abstract fun isWritable(): Boolean

    /**
     * Creates an [InputStream] that allows data to be read from the given
     * file. The input stream should be closed after use.
     *
     * @return the input stream
     * @throws IOException if the file cannot be read from
     */
    @Throws(IOException::class)
    abstract fun openInputStream(): InputStream

    /**
     * Creates an [OutputStream] that allows data to be saved to the given
     * file. If the file does not yet exist, then a new file is created. The
     * output stream should be closed after use.
     *
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     * @throws IOException if the file cannot be written to
     */
    @Throws(IOException::class)
    abstract fun openOutputStream(append: Boolean): OutputStream

    // Helper Methods

    /**
     * Gets the URL represented by this path, if it exists.
     *
     * @return the path's URL
     */
    abstract fun getURL(): URL?

    /**
     * Gets the file represented by this path.
     *
     * @return the path's URL
     */
    fun getFile(): File = File(filename)

    protected fun assertReadable() {
        if (!isReadable()) throw IOException("$this is not readable")
    }

    protected fun assertWritable() {
        if (!isWritable()) throw IOException("$this is not writable")
    }

    override fun toString(): String = "${javaClass.simpleName} \"$filename\""

}