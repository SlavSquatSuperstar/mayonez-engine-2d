package mayonez.io

import mayonez.io.*
import mayonez.util.*
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * The location of a file on the computer, which defines its file
 * permissions.
 */
enum class LocationType {

    /**
     * A system resource inside the JAR that is read-only. Classpath filenames
     * must use '/' separators regardless of the parent operating system.
     */
    CLASSPATH {
        override fun getFilename(filename: String): String {
            return OperatingSystem.LINUX.getOSFilename(filename)
        }

        override fun getURL(filename: String): URL? {
            return ClassLoader.getSystemResource(getFilename(filename))
        }

        override fun openInputStream(filename: String): InputStream {
            return ClassLoader.getSystemResourceAsStream(filename)
                ?: throw IOException("Classpath resource $filename could not be read")
        }

        override fun openOutputStream(filename: String, append: Boolean): OutputStream {
            throw IOException("Classpath files cannot be written to")
        }
    },

    /**
     * A file outside the JAR and in the local device that is readable and
     * writable. External filenames use the file separators of their parent
     * operating system.
     */
    EXTERNAL {
        override fun getFilename(filename: String): String {
            return OperatingSystem.getCurrentOSFilename(filename)
        }

        override fun getURL(filename: String): URL? {
            return try {
                File(filename).toURI().toURL()
            } catch (e: MalformedURLException) {
                null
            }
        }

        override fun openInputStream(filename: String): InputStream {
            try {
                return Files.newInputStream(Paths.get(filename))
            } catch (e: Exception) {
                throw IOException("External file $filename could not be read")
            }
        }

        override fun openOutputStream(filename: String, append: Boolean): OutputStream {
            val options = if (append) {
                arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            } else {
                arrayOf(StandardOpenOption.CREATE)
            }

            try {
                return Files.newOutputStream(Paths.get(filename), *options)
            } catch (e: Exception) {
                throw IOException("External file $filename could not be saved")
            }
        }
    };

    // Filename Methods

    abstract fun getFilename(filename: String): String

    /**
     * Locates file according to the given location type.
     *
     * @param filename the file's OS name
     * @return the file's URL, if it exists
     */
    abstract fun getURL(filename: String): URL?

    // Stream Methods

    /**
     * Creates an [InputStream] that allows data to be read from the given
     * file. The input stream should be closed after use.
     *
     * @param filename the file's OS name
     * @return the input stream
     * @throws IOException if the file cannot be read from
     */
    @Throws(IOException::class)
    abstract fun openInputStream(filename: String): InputStream

    /**
     * Creates an [OutputStream] that allows data to be saved to the given
     * file. If the file does not yet exist, then a new file is created. The
     * output stream should be closed after use.
     *
     * @param filename the file's OS name
     * @param append whether to add data to an existing file's contents instead
     *     of overwriting it
     * @return the output stream
     * @throws IOException if the file cannot be written to
     */
    @Throws(IOException::class)
    abstract fun openOutputStream(filename: String, append: Boolean): OutputStream

    override fun toString(): String = StringUtils.capitalizeFirstWord(name)

}