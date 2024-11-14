package mayonez.assets

import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * A file outside the JAR and in the local device that is readable and
 * writable. External filenames use the file separators of their parent
 * operating system.
 *
 * @author SlavSquatSuperstar
 */
class ExternalFilePath(filename: String) : FilePath(filename.toExternal()) {

    override fun exists(): Boolean = getFile().exists()

    override fun isReadable(): Boolean = getFile().isFile

    override fun isWritable(): Boolean {
        val file = getFile() // Check that parent folder exists
        return file.parentFile.isDirectory && !file.isDirectory
    }

    override fun openInputStream(): InputStream {
        assertReadable()
        try {
            return Files.newInputStream(Paths.get(filename))
        } catch (_: Exception) {
            throw IOException("Could not open input stream for $this")
        }
    }

    override fun openOutputStream(append: Boolean): OutputStream {
        assertWritable()
        val options = if (append) {
            arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        } else {
            arrayOf(StandardOpenOption.CREATE)
        }
        try {
            return Files.newOutputStream(Paths.get(filename), *options)
        } catch (_: Exception) {
            throw IOException("Could not open output stream for $this")
        }
    }

    override fun getURL(): URL? {
        return try {
            File(filename).toURI().toURL()
        } catch (_: MalformedURLException) {
            null
        }
    }

    override val typeName: String
        get() = "External"

}

private fun String.toExternal(): String {
    return PathUtil.convertPath(this)
}