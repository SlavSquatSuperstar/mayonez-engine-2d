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

    // Path Methods

    override fun isDirectory(): Boolean {
        return url != null && getFile()!!.isDirectory
    }

    override fun isFile(): Boolean {
        return url != null && getFile()!!.isFile
    }

    // Conversion Methods

    override fun getFile(): File? {
        return if (url != null) File(url.path) // Absolute path
        else null
    }

    // Scanner Methods

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

}