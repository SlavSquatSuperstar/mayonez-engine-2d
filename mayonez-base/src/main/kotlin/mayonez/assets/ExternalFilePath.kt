package mayonez.assets

import java.io.*
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

    // Path Methods

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