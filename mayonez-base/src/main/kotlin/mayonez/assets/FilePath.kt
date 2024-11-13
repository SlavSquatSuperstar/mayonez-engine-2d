package mayonez.assets

import mayonez.io.*
import java.io.File
import java.net.URL

/**
 * Represents the location of an [mayonez.assets.Asset] on the computer's
 * file system and describes whether it is readable or writable.
 *
 * @author SlavSquatSuperstar
 */
class FilePath(filename: String) {

    // Filename Fields

    /**
     * The OS-independent filename separated by forward slashes ('/'), used to
     * store files in the asset system.
     */
    val filename: String = LocationType.CLASSPATH.getFilename(filename)
    private val osFilename: String = LocationType.EXTERNAL.getFilename(filename)
    var locationType: LocationType = getLocationType(this.filename)
        private set


    // Test Constructor
    internal constructor(filename: String, locationType: LocationType) : this(filename) {
        this.locationType = locationType
    }

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

    // Conversion Methods

    private fun getFile(): File = File(osFilename)

    private fun getURL(): URL? = locationType.getURL(filename)

    override fun toString(): String = filename

}

// Helper Methods

/**
 * Automatically determines the file's location type. Attempts to locate a
 * classpath resource at this path, or otherwise defaults to an external
 * file.
 */
private fun getLocationType(filename: String): LocationType {
    return if (LocationType.CLASSPATH.getURL(filename) != null) {
        LocationType.CLASSPATH
    } else {
        LocationType.EXTERNAL
    }
}