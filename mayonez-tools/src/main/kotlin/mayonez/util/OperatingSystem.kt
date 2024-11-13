package mayonez.util

/**
 * An operating system of a computer running Java.
 *
 * @author SlavSquatSuperstar
 */
enum class OperatingSystem(private val osName: String) {

    /** The FreeBSD operating system. */
    FREE_BSD("FreeBSD"),

    /** The GNU/Linux family of operating systems. */
    LINUX("Linux"),

    /** The macOS/OS X operating system. */
    MAC_OS("macOS"),

    /** The Microsoft Windows family of operating systems. */
    WINDOWS("Windows"),

    /** An unknown or undefined operating system. */
    UNKNOWN("Unknown OS");

    override fun toString(): String = osName

    companion object {
        private lateinit var current: OperatingSystem

        /**
         * Gets the current operating system of this device running Java.
         *
         * @return the current OS.
         */
        @JvmStatic
        fun getCurrent(): OperatingSystem {
            if (!OperatingSystem::current.isInitialized) {
                current = getFromName(System.getProperty("os.name").lowercase())
            }
            return current
        }

        private fun getFromName(osName: String): OperatingSystem {
            return if (osName.contains("bsd")) {
                FREE_BSD
            } else if (osName.contains("linux")) {
                LINUX
            } else if (osName.contains("mac") || osName.contains("darwin")) {
                MAC_OS
            } else if (osName.contains("windows")) {
                WINDOWS
            } else {
                UNKNOWN
            }
        }
    }

}