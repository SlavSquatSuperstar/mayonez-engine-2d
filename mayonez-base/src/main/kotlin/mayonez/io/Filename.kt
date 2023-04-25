package mayonez.io

import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import kotlin.io.path.pathString

class Filename(filename: String, val location: AssetLocation) {

    constructor(filename: String) : this(filename, getLocation(getOSFilename(filename)))

    val name = getOSFilename(filename)

    // I/O Status Methods

    /**
     * Whether this there a file or directory that currently exists at this
     * location.
     *
     * @return if there is a valid file at this path
     */
    fun exists(): Boolean {
        return when (location) {
            AssetLocation.CLASSPATH -> getURL() != null
            AssetLocation.EXTERNAL -> getFile().exists()
        }
    }

    /**
     * Whether this there a file with this filename that exists and can be read
     * from. Returns false if the file is a directory.
     *
     * @return if there is a readable file at this path
     */
    fun isReadable(): Boolean {
        return when (location) {
            AssetLocation.CLASSPATH -> getURL() != null
            AssetLocation.EXTERNAL -> getFile().isFile
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
        return when (location) {
            AssetLocation.CLASSPATH -> false
            AssetLocation.EXTERNAL -> !getFile().isDirectory
        }
    }

    // Conversion Methods

    fun getFile(): File = File(name)

    private fun getURL(): URL? {
        return when (location) {
            AssetLocation.CLASSPATH -> Asset.getClasspathURL(name)
            AssetLocation.EXTERNAL -> Asset.getFileURL(name)
        }
    }

    companion object {
        private fun getOSFilename(filename: String): String {
            return Paths.get(filename).pathString
        }

        private fun getLocation(filename: String): AssetLocation {
            return if (getClasspathURL(filename) != null) AssetLocation.CLASSPATH
            else AssetLocation.EXTERNAL
        }

        /**
         * Attempts to locate a classpath resource from within the JAR executable.
         *
         * @param path the file's location inside the JAR's root, ot null if not
         *     present
         * @return the resource's URL
         */
        private fun getClasspathURL(path: String): URL? = ClassLoader.getSystemResource(path)

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