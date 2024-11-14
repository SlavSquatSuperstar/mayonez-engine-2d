package mayonez.assets

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
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

    override fun isWritable(): Boolean = !getFile().isDirectory

    override fun openInputStream(): InputStream {
        assertReadable()
        try {
            return Files.newInputStream(Paths.get(filename))
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            throw IOException("Could not open output stream for $this")
        }
    }

    override fun getURL(): URL? {
        return try {
            File(filename).toURI().toURL()
        } catch (e: MalformedURLException) {
            null
        }
    }

    override val typeName: String
        get() = "External"

}

private fun String.toExternal(): String {
    return PathUtil.convertPath(this)
}