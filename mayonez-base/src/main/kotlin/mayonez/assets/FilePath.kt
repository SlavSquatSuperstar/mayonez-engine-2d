package mayonez.assets

import mayonez.io.LocationType
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
class FilePath(filename: String, val locationType: LocationType) {

    /**
     * Create a FilePath and automatically determines the location type.
     * Attempts to locate a classpath resource at this path, or otherwise defaults
     * to an external file.
     */
    constructor(filename: String) : this(filename, filename.getLocationType())

    // Create filename with correct separators
    val filename: String = locationType.getFilename(filename)

    // I/O Status Methods

    /**
     * Whether this there a file or directory that currently exists at this
     * location.
     *
     * @return if there is a valid file at this path
     */
    fun exists(): Boolean {
        return when (locationType) {
            LocationType.CLASSPATH -> getURL() != null
            LocationType.EXTERNAL -> getFile().exists()
        }
    }

    /**
     * Whether this there a file with this filename that exists and can be read
     * from. Returns false if the file is a directory.
     *
     * @return if there is a readable file at this path
     */
    fun isReadable(): Boolean {
        return when (locationType) {
            LocationType.CLASSPATH -> getURL() != null
            LocationType.EXTERNAL -> getFile().isFile
        }
    }

    /**
     * Whether this there a file with this filename that can be written to.
     * Classpath files and all directories are not writable, and non-existing
     * external files may be created.
     *
     * @return if there is a readable file at this path
     */
    fun isWritable(): Boolean {
        return when (locationType) {
            LocationType.CLASSPATH -> false
            LocationType.EXTERNAL -> !getFile().isDirectory
        }
    }

    // Stream Methods

    /**
     * Creates an [InputStream] that allows data to be read from the given
     * file. The input stream should be closed after use.
     *
     * @return the input stream
     * @throws IOException if the file cannot be read from
     */
    @Throws(IOException::class)
    fun openInputStream(): InputStream {
        if (!isReadable()) {
            throw IOException("$locationType asset $filename is not readable")
        }
        return locationType.openInputStream(filename)
    }

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
    fun openOutputStream(append: Boolean): OutputStream {
        if (!isWritable()) {
            throw IOException("$locationType path $filename is not writable")
        }
        return locationType.openOutputStream(filename, append)
    }

    // Conversion Methods

    private fun getFile(): File = File(filename) // Should only be called when external

    private fun getURL(): URL? = locationType.getURL(filename) // Should only be called when classpath

    override fun toString(): String = "$locationType FilePath $filename"

}

private fun String.getLocationType(): LocationType {
    return if (LocationType.CLASSPATH.getURL(this) != null) {
        LocationType.CLASSPATH
    } else {
        LocationType.EXTERNAL
    }
}