import org.gradle.internal.os.OperatingSystem

/**
 * Detects the user's OS and architecture and configures the suffix for the
 * LWJGL native libraries.
 *
 * @author SlavSquatSuperstar
 *
 * Source: https://www.lwjgl.org/customize -> download build.gradle(.kts)
 */
object Natives {
    const val FREE_BSD = "natives-freebsd"

    const val LINUX_X64 = "natives-linux"
    const val LINUX_ARM64 = "natives-linux-arm64"
    const val LINUX_ARM32 = "natives-linux-arm32"

    const val MAC_OS_X64 = "natives-macos" // macOS Intel
    const val MAC_OS_ARM64 = "natives-macos-arm64" // macOS Apple Silicon

    const val WINDOWS_X64 = "natives-windows"
    const val WINDOWS_X86 = "natives-windows-x86"
    const val WINDOWS_ARM64 = "natives-windows-arm64"

    /**
     * Get the LWJGL natives string for the user's current OS and architecture.
     */
    fun getDefaultNatives(): String {
        val osArch = System.getProperty("os.arch")
        return when (OperatingSystem.current()) {
            OperatingSystem.FREE_BSD -> {
                FREE_BSD
            }

            OperatingSystem.LINUX -> {
                if (osArch.isARM()) {
                    if (osArch.is64Bit()) LINUX_ARM64
                    else LINUX_ARM32
                } else {
                    LINUX_X64
                }
            }

            OperatingSystem.MAC_OS -> {
                if (osArch.isARM()) MAC_OS_ARM64
                else MAC_OS_X64
            }

            OperatingSystem.WINDOWS -> {
                if (osArch.is64Bit()) {
                    if (osArch.isARM()) WINDOWS_ARM64
                    else WINDOWS_X64
                } else {
                    WINDOWS_X86
                }
            }

            else -> LINUX_X64 // Generic Unix
        }
    }

    /** Whether the user is running macOS (for LWJGL VM args). */
    internal fun isMacOS(): Boolean {
        return OperatingSystem.current() == OperatingSystem.MAC_OS
    }
}

private fun String.is64Bit(): Boolean {
    return this.contains("64") || this.startsWith("armv8")
}

private fun String.isARM(): Boolean {
    return this.startsWith("arm") || this.startsWith("aarch64")
}