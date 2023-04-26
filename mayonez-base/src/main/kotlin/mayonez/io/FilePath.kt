package mayonez.io

import mayonez.util.*
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import kotlin.io.path.pathString

/**
 * Represents the location of an [mayonez.io.Asset] on the computer's file
 * system and describes whether it is readable or writable.
 */
class FilePath(filename: String, val locationType: LocationType) {

    constructor(filename: String) : this(filename, getLocationType(filename))

    val filename = getOSFilename(filename)

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

    private fun getFile(): File = File(filename)

    private fun getURL(): URL? {
        return when (locationType) {
            LocationType.CLASSPATH -> getClasspathURL(filename)
            LocationType.EXTERNAL -> getExternalURL(filename)
        }
    }

    override fun toString(): String = filename

    companion object {

        /**
         * Gets the filename with the correct path separators for the current OS.
         * Note that '/' and '\' are valid filename characters in macOS, and may be
         * incorrectly replaced.
         *
         * @param filename a path to a file
         * @return the path formatted for the OS
         */
        @JvmStatic
        fun getOSFilename(filename: String): String {
            val pathString = Paths.get(filename).pathString // remove terminal '/' for folders
            val sep = OperatingSystem.getCurrentOS().fileSeparator // correct any separators
            val osName = pathString.split("/", "\\").joinToString(sep)
            return osName
        }

        /**
         * Automatically determines the filename's location type. Attempts to
         * locate a classpath resource at this path, or otherwise defaults to an
         * external file.
         */
        private fun getLocationType(filename: String): LocationType {
            val osFilename = getOSFilename(filename)
            return if (getClasspathURL(osFilename) != null) LocationType.CLASSPATH
            else LocationType.EXTERNAL
        }

        /**
         * Attempts to locate a classpath resource from within the JAR executable.
         *
         * @param filename the file's location inside the JAR's root, ot null if
         *     not present
         * @return the resource's URL
         */
        private fun getClasspathURL(filename: String): URL? = ClassLoader.getSystemResource(filename)

        /**
         * Locates an external file from outside the JAR executable.
         *
         * @param filename the file's location inside the computer's local storage
         * @return the file's URL
         */
        private fun getExternalURL(filename: String): URL? {
            return try {
                File(filename).toURI().toURL()
            } catch (e: MalformedURLException) {
                null
            }
        }

    }

}