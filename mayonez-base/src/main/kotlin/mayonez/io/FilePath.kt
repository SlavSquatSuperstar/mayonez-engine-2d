package mayonez.io

import mayonez.util.*
import java.io.File
import java.net.MalformedURLException
import java.net.URL

/**
 * Represents the location of an [mayonez.io.Asset] on the computer's file
 * system and describes whether it is readable or writable.
 */
class FilePath(filename: String) {

    /**
     * The OS-independent filename separated by forward slashes ('/'), used to store
     * files in the asset system.
     */
    val filename: String = getClasspathFilename(filename)
    private val osFilename: String = getOSFilename(filename)
    var locationType: LocationType = getLocationType(this.filename)
        private set

    constructor(filename: String, locationType: LocationType) : this(filename) {
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

    private fun getURL(): URL? {
        return when (locationType) {
            LocationType.CLASSPATH -> getClasspathURL(filename)
            LocationType.EXTERNAL -> getExternalURL(osFilename)
        }
    }

    override fun toString(): String = filename

    // Helper Methods

    companion object {

        /**
         * Gets the filename with the correct path separators for the current OS.
         * Note that '/' and '\' are valid filename characters in Unix, and may be
         * incorrectly replaced.
         *
         * @param filename a path to a file
         * @return the path formatted for the OS
         */
        @JvmStatic
        fun getOSFilename(filename: String): String {
            val currOS = OperatingSystem.getCurrentOS()
            return currOS.getOSFilename(filename)
        }

        private fun getClasspathFilename(filename: String): String {
            return OperatingSystem.LINUX.getOSFilename(filename)
        }

        /**
         * Automatically determines the file's location type. Attempts to
         * locate a classpath resource at this path, or otherwise defaults to an
         * external file.
         */
        private fun getLocationType(filename: String): LocationType {
            return if (getClasspathURL(filename) != null) LocationType.CLASSPATH
            else LocationType.EXTERNAL
        }

        /**
         * Attempts to locate a classpath resource from within the JAR executable.
         * A classpath must use '/' separators despite the operating system.
         *
         * @param filename the file's location inside the JAR's root, ot null if
         *     not present
         * @return the resource's URL
         */
        @JvmStatic
        fun getClasspathURL(filename: String): URL? {
            val resourcePath = OperatingSystem.LINUX.getOSFilename(filename)
            return ClassLoader.getSystemResource(resourcePath)
        }

        /**
         * Locates an external file from outside the JAR executable.
         *
         * @param filename the file's location inside the computer's local storage
         * @return the file's URL
         */
        @JvmStatic
        fun getExternalURL(filename: String): URL? {
            return try {
                File(filename).toURI().toURL()
            } catch (e: MalformedURLException) {
                null
            }
        }

    }

}