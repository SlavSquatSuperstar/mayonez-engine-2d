package mayonez.assets

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.jar.JarFile

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

    // Path is local if protocol is not inside jar
    private val isLocal: Boolean = url?.protocol == "file"

    // Path Methods

    override fun exists(): Boolean = url != null

    override fun isReadable(): Boolean = url != null

    override fun isWritable(): Boolean = false

    override fun isDirectory(): Boolean {
        return url != null &&
                if (isLocal) getFile().isDirectory
                else getJarFile()!!.getJarEntry(filename)!!.isDirectory
    }

    override fun isFile(): Boolean {
        return url != null &&
                if (isLocal) getFile().isFile
                else !getJarFile()!!.getJarEntry(filename)!!.isDirectory
    }

    private fun getJarFile(): JarFile? {
        // Get the parent jar file
        val path = url?.path ?: return null // file:/path/to/jar!/name
        val jarName = path.substring(
            path.indexOf(":") + 1, path.indexOf("!/")
        ) // /path/to/jar
        return JarFile(jarName)
    }

    // File Methods

    override fun createFile(): Boolean = false

    override fun createDirectory(): Boolean = false

    override fun delete(): Boolean = false

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

    override fun getFile(): File {
        return if (isLocal) File(url!!.path) // Absolute path
        else File(filename) // Meaningless
    }

    override fun getURL(): URL? = url

    override val typeName: String
        get() = "Classpath"

}