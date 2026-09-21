package mayonez.assets

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/**
 * A classpath resource inside a .jar or source set. Classpath resources cannot
 * be written to, as their location may change depending on the environment.
 * When running the program using a build system, resources will usually be
 * [LocalClasspathFilePath]s, and when running the program from a .jar, they
 * will typically be [JarClasspathFilePath]s.
 *
 * Classpath paths must use '/' separators regardless of the parent
 * operating system.
 *
 * @author SlavSquatSuperstar
 */
abstract class ClasspathFilePath protected constructor(path: String) :
    FilePath(PathUtil.convertPath(path, PathUtil.CLASSPATH_SEPARATOR)) {

    /** The URL to this resource, which is non-null iff the resource exists. */
    protected val url: URL? = PathUtil.getResourceURL(path)

    // File Status Methods

    override fun exists(): Boolean = url != null

    override fun isReadable(): Boolean = url != null

    override fun isWritable(): Boolean = false

    // File Methods

    override fun createFile(): Boolean = false

    override fun createDirectory(): Boolean = false

    override fun delete(): Boolean = false

    // Stream Methods

    @Throws(IOException::class)
    override fun openInputStream(): InputStream {
        if (!isReadable()) throw IOException("$this is not readable")
        return url?.openStream()
            ?: throw IOException("Could not open input stream for $this")
    }

    @Throws(IOException::class)
    override fun openOutputStream(append: Boolean): OutputStream {
        throw IOException("Classpath resources are read-only")
    }

    // Conversion Methods

    override fun getURL(): URL? = url

    override val typeName: String
        get() = "Classpath"

}