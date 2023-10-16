package mayonez.assets

import mayonez.io.*
import java.io.File
import java.net.URL

/**
 * Represents the location of an [mayonez.assets.Asset] on the computer's
 * file system and describes whether it is readable or writable.
 */
class FilePath(filename: String) {

    // Constructors
    constructor(filename: String, locationType: LocationType) : this(filename) {
        this.locationType = locationType
    }

    // Filename Fields

    /**
     * The OS-independent filename separated by forward slashes ('/'), used to
     * store files in the asset system.
     */
    val filename: String = getClasspathFilename(filename)
    private val osFilename: String = getOSFilename(filename)
    var locationType: LocationType = getLocationType(this.filename)
        private set

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

private fun getOSFilename(filename: String): String {
    return OperatingSystem.getCurrentOSFilename(filename)
}

private fun getClasspathFilename(filename: String): String {
    return OperatingSystem.LINUX.getOSFilename(filename)
}

/**
 * Automatically determines the file's location type. Attempts to locate a
 * classpath resource at this path, or otherwise defaults to an external
 * file.
 */
private fun getLocationType(filename: String): LocationType {
    return if (LocationType.CLASSPATH.getURL(filename) != null) {
        LocationType.CLASSPATH
    } else LocationType.EXTERNAL
}