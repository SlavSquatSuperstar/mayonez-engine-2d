package mayonez.assets

import java.io.File
import java.util.jar.*

/**
 * A read-only classpath resource inside the current .jar or a classpath .jar.
 * Classpath filenames must use '/' separators regardless of the parent
 * operating system.
 *
 * @author SlavSquatSuperstar
 */
class JarClasspathFilePath(filename: String) : ClasspathFilePath(filename) {

    // Path Methods

    override fun isDirectory(): Boolean {
        return url != null && getJarEntry().isDirectory
    }

    override fun isFile(): Boolean {
        return url != null && !getJarEntry().isDirectory
    }

    private fun getJarFile(): JarFile? {
        if (url == null) return null

        // Get the parent jar file path on the external file system
        // No need to convert / to \
        val path = PathUtil.decodeURL(url) // file:/path/to/jar!/name
        val jarPath = path.substring(
            path.indexOf(":") + 1, path.indexOf("!/")
        ) // /path/to/jar
        return JarFile(jarPath)
    }

    private fun getJarEntry(): JarEntry {
        // No need to decode URL since using filename
        return getJarFile()!!.getJarEntry(filename)!!
    }

    // Conversion Methods

    override fun getFile(): File? = null

    // Scanner Methods

    override fun scanFiles(): List<FilePath> {
        if (!isDirectory()) return emptyList()

        // Search jar entries inside this directory
        return getJarFile()!!.stream()
            .filter {
                !it.isDirectory
                        && !it.name.contains(".DS_Store")
                        && it.name.startsWith(filename)
            }
            .map { JarClasspathFilePath(it.name) }
            .toList()
    }

}