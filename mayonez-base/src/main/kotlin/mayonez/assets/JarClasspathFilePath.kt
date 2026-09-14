package mayonez.assets

import java.io.File
import java.util.jar.JarFile

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
        return url != null &&
                getJarFile()!!.getJarEntry(filename)!!.isDirectory
    }

    override fun isFile(): Boolean {
        return url != null &&
                !getJarFile()!!.getJarEntry(filename)!!.isDirectory
    }

    private fun getJarFile(): JarFile? {
        // Get the parent jar file
        val path = url?.path ?: return null // file:/path/to/jar!/name
        val jarName = path.substring(
            path.indexOf(":") + 1, path.indexOf("!/")
        ) // /path/to/jar
        return JarFile(jarName)
    }

    // Conversion Methods

    override fun getFile(): File? = null

}