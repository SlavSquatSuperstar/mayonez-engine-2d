package mayonez.assets

import java.io.File

/**
 * A classpath resource inside one of the source sets on the local file system.
 * Local classpath files should not be written to, as doing so may affect the
 * project build. Classpath filenames must use '/' separators regardless of the
 * parent operating system.
 *
 * @author SlavSquatSuperstar
 */
class LocalClasspathFilePath(filename: String) : ClasspathFilePath(filename) {

    // Path Methods

    override fun isDirectory(): Boolean {
        return url != null && getFile()!!.isDirectory
    }

    override fun isFile(): Boolean {
        return url != null && getFile()!!.isFile
    }

    // Conversion Methods

    override fun getFile(): File? {
        return if (url != null) File(url.path) // Absolute path
        else null
    }

}