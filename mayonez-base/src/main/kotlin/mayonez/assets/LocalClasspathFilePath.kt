package mayonez.assets

import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.io.path.name

/**
 * A classpath resource inside one of the source sets on the local file system.
 * Local classpath files should not be written to, as doing so may affect the
 * project build. Classpath filenames must use '/' separators regardless of the
 * parent operating system.
 *
 * @author SlavSquatSuperstar
 */
class LocalClasspathFilePath(filename: String) : ClasspathFilePath(filename) {

    // File Status Methods

    override fun isDirectory(): Boolean {
        return url != null && getFile()!!.isDirectory
    }

    override fun isFile(): Boolean {
        return url != null && getFile()!!.isFile
    }

    // File Hierarchy Methods

    override fun getParent(): FilePath? {
        if (url == null) return null

        // Already normalized
        val idx = filename.lastIndexOf("/")
        return if (idx == -1) null
        else LocalClasspathFilePath(filename.substring(0, idx))
    }

    override fun combine(path: String?): FilePath? {
        return if (path == null) null
        // Constructor normalizes path
        else LocalClasspathFilePath("$filename/$path")
    }

    override fun scanFiles(): List<FilePath> {
        if (!isDirectory()) return emptyList() // If not directory return empty list

        // Use Files.walk() to get recursive tree
        // Seems to skip .DS_Store, but filter just in case
        val file = getFile()!!
        return try {
            Files.walk(file.toPath())
                .filter { Files.isRegularFile(it) && it.name != ".DS_Store" }
                .map { it.toFile().relativeTo(file) } // Get relative path
                .map { "$filename/${it.path}" } // Combine with base
                .map { LocalClasspathFilePath(it) }
                .toList()
        } catch (_: IOException) {
            emptyList()
        }
    }

    // Conversion Methods

    override fun getFile(): File? {
        return if (url == null) null
        else if (PathUtil.CURRENT_SEPARATOR == PathUtil.UNIX_SEPARATOR) {
            // Get absolute path, and keep /
            File(PathUtil.decodeURL(url))
        } else {
            // Get absolute path, remove \C:, and convert / to \
            val decoded = PathUtil.decodeURL(url)
            val windowsPath = PathUtil.convertPath(
                decoded.substring(3), PathUtil.WINDOWS_SEPARATOR
            )
            File(windowsPath)
        }
    }

}