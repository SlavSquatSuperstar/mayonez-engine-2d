package mayonez.io

import java.util.*

/**
 * An operating system of a computer running Java.
 *
 * @author SlavSquatSuperstar
 */
enum class OperatingSystem(private val osName: String) {

    /** The GNU/Linux family of operating systems. */
    LINUX("Linux"),

    /** The macOS or OS X family of operating systems. */
    MAC_OS("macOS"),

    /** The Microsoft Windows family of operating systems. */
    WINDOWS("Windows"),

    /** An unknown or undefined operating system. */
    UNKNOWN("Unknown OS");

    override fun toString(): String = osName

    companion object {
        private lateinit var currentOS: OperatingSystem

        /**
         * Gets the current operating system of this device running Java.
         *
         * @return the current OS.
         */
        @JvmStatic
        fun getCurrentOS(): OperatingSystem {
            if (OperatingSystem::currentOS.isInitialized) return currentOS

            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            currentOS = when {
                osName.contains("linux") -> LINUX
                osName.contains("mac") -> MAC_OS
                osName.contains("windows") -> WINDOWS
                else -> UNKNOWN
            }
            return currentOS
        }
    }

}
