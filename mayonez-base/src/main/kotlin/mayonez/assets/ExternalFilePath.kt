package mayonez.assets

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * A file outside the .jar and in the local file system that is readable and
 * writable. External filenames use the file separators of their parent
 * operating system.
 *
 * @author SlavSquatSuperstar
 */
class ExternalFilePath(filename: String) : FilePath(PathUtil.convertPath(filename)) {

    // File exists iff path exists
    private val file: File = File(filename)

    // File Status Methods

    override fun exists(): Boolean = file.exists()

    override fun isReadable(): Boolean = file.isFile

    override fun isWritable(): Boolean {
        // Check that parent folder exists
        return file.parentFile.isDirectory && !file.isDirectory
    }

    override fun isDirectory(): Boolean = file.isDirectory

    override fun isFile(): Boolean = file.isFile

    // File Methods

    override fun createFile(): Boolean {
        return try {
            file.createNewFile()
        } catch (_: Exception) {
            false
        }
    }

    override fun createDirectory(): Boolean {
        return try {
            file.mkdir()
        } catch (_: Exception) {
            false
        }
    }

    override fun delete(): Boolean {
        return try {
            file.delete()
        } catch (_: Exception) {
            false
        }
    }

    // Stream Methods

    @Throws(IOException::class)
    override fun openInputStream(): InputStream {
        assertReadable()
        try {
            return Files.newInputStream(file.toPath())
        } catch (_: Exception) {
            throw IOException("Could not open input stream for $this")
        }
    }

    @Throws(IOException::class)
    override fun openOutputStream(append: Boolean): OutputStream {
        assertWritable()
        val options = if (append) {
            arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        } else {
            arrayOf(StandardOpenOption.CREATE)
        }
        try {
            return Files.newOutputStream(file.toPath(), *options)
        } catch (_: Exception) {
            throw IOException("Could not open output stream for $this")
        }
    }

    // File Tree Methods

    override fun getParent(): FilePath? {
        return ExternalFilePath(file.parent?: return null)
    }

    override fun combine(path: String?): FilePath? {
        val child = File(file, path ?: return null)
        return ExternalFilePath(child.path)
    }

    override fun scanFiles(): List<FilePath> {
        if (!isDirectory()) return emptyList() // If not directory return empty list

        // Use Files.walk() to get recursive tree
        return try {
            Files.walk(file.toPath())
                .filter { Files.isRegularFile(it) }
                .map { it.toFile().path }
                .filter { !it.contains(".DS_Store") }
                .map { ExternalFilePath(it) }
                .toList()
        } catch (_: IOException) {
            emptyList()
        }
    }

    // Conversion Methods

    override fun getFile(): File = file

    override fun getURL(): URL? {
        return try {
            file.toURI().toURL()
        } catch (_: MalformedURLException) {
            null // Should not occur
        }
    }

    override val typeName: String
        get() = "External"

}