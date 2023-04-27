package mayonez.util

import java.nio.file.Paths
import java.util.*
import kotlin.io.path.pathString

/**
 * An operating system of a computer running Java. Each operating system defines a file separator
 * and a line separator.
 */
enum class OperatingSystem(
    private val osName: String,
    val fileSeparator: String,
    val lineSeparator: String,
    val isUnix: Boolean
) {
    /**
     * The GNU/Linux family of operating systems.
     */
    LINUX("Linux", "/", "\n", true),

    /**
     * The macOS or OS X family of operating systems.
     */
    MAC_OS("Mac OS", "/", "\n", true),

    /**
     * The Microsoft Windows family of operating systems.
     */
    WINDOWS("Windows", "\\", "\r\n", false),

    /**
     * An unknown or undefined operating system.
     */
    UNKNOWN("Unknown", "/", "\n", true);

    fun getOSFilename(filename: String): String {
        val pathString = Paths.get(filename).pathString // remove terminal '/' for folders
        return pathString.split(*separators).joinToString(fileSeparator)
    }

    override fun toString(): String = osName

    companion object {

        private val separators: Array<String> = arrayOf("/", "\\")

        /**
         * Gets the current operating system of this device running Java.
         *
         * @return the current OS.
         */
        @JvmStatic
        fun getCurrentOS(): OperatingSystem {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())

            return if (osName.contains("linux")) {
                LINUX
            } else if (osName.contains("mac")) {
                MAC_OS
            } else if (osName.contains("windows")) {
                WINDOWS
            } else {
                UNKNOWN
            }
        }
    }
}
