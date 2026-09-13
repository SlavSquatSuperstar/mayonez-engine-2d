package mayonez.assets

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/**
 * A system resource inside the .jar that is read-only. Classpath filenames
 * must use '/' separators regardless of the parent operating system.
 *
 * @author SlavSquatSuperstar
 */
class ClasspathFilePath(filename: String) :
    FilePath(PathUtil.convertPath(filename, PathUtil.CLASSPATH_SEPARATOR)) {

    // URL non-null iff resource exists
    private val url: URL? = ClassLoader.getSystemResource(filename)

    // Path Methods

    override fun exists(): Boolean = url != null

    override fun isReadable(): Boolean = url != null

    override fun isWritable(): Boolean = false

    // Stream Methods

    @Throws(IOException::class)
    override fun openInputStream(): InputStream {
        assertReadable()
        return ClassLoader.getSystemResourceAsStream(filename)
            ?: throw IOException("Could not open input stream for $this")
    }

    @Throws(IOException::class)
    override fun openOutputStream(append: Boolean): OutputStream {
        throw IOException("Classpath resources are read-only")
    }

    override fun getFile(): File = File(filename)

    override fun getURL(): URL? = url

    override val typeName: String
        get() = "Classpath"

}