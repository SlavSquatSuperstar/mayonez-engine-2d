package mayonez.assets

import java.io.*
import java.net.URL

/**
 * Represents the location of a resource on the computer's file system or
 * inside a classpath .jar file and facilitates read and write operations.
 * To create a `FilePath`, use [FilePath.of] rather than instantiating
 * directly.
 *
 * @author SlavSquatSuperstar
 */
abstract class FilePath protected constructor(
    /** The normalized string representation of this path. */
    val path: String
) {

    /**
     * The filename of this path, or the last name in the path without any of
     * the parent directories.
     */
    abstract val filename: String

    companion object {
        /**
         * Create a FilePath that best fits the given path string. Returns a
         * classpath resource if one exists at this path, or otherwise
         * defaults to an external file.
         *
         * @param path the path string
         */
        @JvmStatic
        fun of(path: String): FilePath {
            val url = PathUtil.getResourceURL(path)
            return if (url == null) ExternalFilePath(path)
            else if (url.protocol == "file") LocalClasspathFilePath(path)
            else JarClasspathFilePath(path)
        }
    }

    // File Status Methods

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

    /**
     * Whether this exists a directory at this path.
     *
     * @return if this path represents a directory
     */
    abstract fun isDirectory(): Boolean

    /**
     * Whether this exists a normal file at this path.
     *
     * @return if this path represents a file
     */
    abstract fun isFile(): Boolean

    // File Methods

    /**
     * Create an empty normal file at this path if the path is writable, no
     * file exists, and the parent directory exists.
     *
     * @return if the file was created
     */
    abstract fun createFile(): Boolean

    /**
     * Create an empty directory at this path if the path is writable, no
     * file exists, and the parent directory exists.
     *
     * @return if the directory was created
     */
    abstract fun createDirectory(): Boolean

    /**
     * Delete a normal file or empty directory this path if the path is
     * writable and a file exists.
     *
     * @return if the file or directory was deleted
     */
    abstract fun delete(): Boolean

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

    // File Tree Methods

    /**
     * Get the parent directory of this path, or null if this path is the root
     * of its file system.
     *
     * @return the parent path, or null
     */
    abstract fun getParent(): FilePath?

    /**
     * Combine this path's name with another path segment and get the resulting
     * path.
     *
     * @param path another path, which may contain file separators
     * @return the combined path, or null if the path is null
     */
    abstract fun combine(path: String?): FilePath?

    /**
     * Recursively searches for files in this directory and all its
     * subdirectories. If this path represents a file or does not exist,
     * returns an empty list.
     *
     * @return the list of file paths, or empty if none found
     */
    abstract fun scanFiles(): List<FilePath>

    // Conversion Methods

    /**
     * Get the file represented by this path, which is non-null if the path is
     * readable or writable on the file system.
     *
     * @return the file
     */
    abstract fun getFile(): File?

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
        return other is FilePath && other.path == this.path
    }

    override fun hashCode(): Int = path.hashCode()

    override fun toString(): String = "${javaClass.simpleName} $path"

}