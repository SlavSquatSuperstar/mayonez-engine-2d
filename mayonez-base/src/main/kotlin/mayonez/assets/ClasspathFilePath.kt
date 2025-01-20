package mayonez.assets

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/**
 * A system resource inside the JAR that is read-only. Classpath filenames
 * must use '/' separators regardless of the parent operating system.
 *
 * @author SlavSquatSuperstar
 */
class ClasspathFilePath(filename: String): FilePath(filename.toClasspath()) {

    override fun exists(): Boolean = getURL() != null

    override fun isReadable(): Boolean = exists()

    override fun isWritable(): Boolean = false

    override fun openInputStream(): InputStream {
        assertReadable()
        return ClassLoader.getSystemResourceAsStream(filename)
            ?: throw IOException("Could not open input stream for $this")
    }

    override fun openOutputStream(append: Boolean): OutputStream {
        throw IOException("Classpath resources are read-only")
    }

    override fun getURL(): URL? {
        return ClassLoader.getSystemResource(filename)
    }

    override val typeName: String
        get() = "Classpath"

}

private fun String.toClasspath(): String {
    return PathUtil.convertPath(this, PathUtil.CLASSPATH_SEPARATOR)
}