package mayonez.io

import java.nio.file.Paths
import java.util.*
import kotlin.io.path.pathString

private val pathSeparators: Array<String> = arrayOf("/", "\\")

/**
 * An operating system of a computer running Java. Each operating system
 * defines a file separator and a line separator.
 *
 * @author SlavSquatSuperstar
 */
enum class OperatingSystem(
    private val osName: String,
    private val fileSeparator: String,
    private val lineSeparator: String
) {

    /** The GNU/Linux family of operating systems. */
    LINUX("Linux", "/", "\n"),

    /** The macOS or OS X family of operating systems. */
    MAC_OS("Mac OS", "/", "\n"),

    /** The Microsoft Windows family of operating systems. */
    WINDOWS("Windows", "\\", "\r\n"),

    /** An unknown or undefined operating system. */
    UNKNOWN("Unknown OS", "/", "\n");

    fun getOSFilename(filename: String): String {
        return filename.replaceSeparators().removeTrailingSeparator()
    }

    private fun String.replaceSeparators(): String {
        val normalized = Paths.get(this).normalize().pathString // remove extra '.' or '..'
        return normalized.split(*pathSeparators).joinToString(fileSeparator)
    }

    private fun String.removeTrailingSeparator(): String {
        return if (this.endsWith(fileSeparator)) {
            // remove trailing '/' or '\'; classloader will complain otherwise
            this.substring(0..<this.length - 1)
        } else this
    }

    override fun toString(): String = osName

    companion object {

        /**
         * Gets the current operating system of this device running Java.
         *
         * @return the current OS.
         */
        @JvmStatic
        fun getCurrentOS(): OperatingSystem {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())

            return when {
                osName.contains("linux") -> LINUX
                osName.contains("mac") -> MAC_OS
                osName.contains("windows") -> WINDOWS
                else -> UNKNOWN
            }
        }

        /**
         * Gets the filename with the correct path separators for the current OS.
         * Note that '/' and '\' are valid filename characters in Unix, and may be
         * incorrectly replaced.
         *
         * @param filename a path to a file
         * @return the path formatted for the OS
         */
        @JvmStatic
        fun getCurrentOSFilename(filename: String): String {
            return getCurrentOS().getOSFilename(filename)
        }

    }

}
