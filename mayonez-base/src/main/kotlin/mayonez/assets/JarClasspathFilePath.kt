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

    // File Status Methods

    override fun isDirectory(): Boolean {
        return getJarEntry()?.isDirectory == true
    }

    override fun isFile(): Boolean {
        return getJarEntry()?.isDirectory == false
    }

    private fun getJarPath(): String? {
        if (url == null) return null

        // Get the parent jar file path on the external file system
        // No need to convert / to \
        val path = PathUtil.decodeURL(url) // file:/path/to/jar!/name
        val jarPath = path.substring(
            path.indexOf(":") + 1, path.indexOf("!/")
        ) // /path/to/jar
        return jarPath
    }

    private fun getJarFile(): JarFile? {
        return JarFile(getJarPath() ?: return null)
    }

    private fun getJarEntry(): JarEntry? {
        // No need to decode URL since using filename
        return getJarFile()?.getJarEntry(filename)
    }

    // File Hierarchy Methods

    override fun getParent(): FilePath? {
        if (url == null) return null

        // Already normalized
        val idx = filename.lastIndexOf("/")
        return if (idx == -1) null
        else JarClasspathFilePath(filename.substring(0, idx))
    }

    override fun scanFiles(): List<FilePath> {
        val jarFile = getJarFile()
        return if (jarFile?.getJarEntry(filename)?.isDirectory != true) {
            emptyList() // Not a directory
        } else {
            // Search jar entries inside this directory
            jarFile.stream()
                .filter {
                    !it.isDirectory
                            && !it.name.contains(".DS_Store")
                            && it.name.startsWith(filename)
                }
                .map { JarClasspathFilePath(it.name) }
                .toList()
        }
    }

    // Conversion Methods

    override fun getFile(): File? = null

}