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

    const val MAC_X64 = "natives-macos" // macOS Intel
    const val MAC_ARM64 = "natives-macos-arm64" // macOS Apple Silicon

    const val WINDOWS_X64 = "natives-windows"
    const val WINDOWS_X86 = "natives-windows-x86"
    const val WINDOWS_ARM64 = "natives-windows-arm64"

    /**
     * Get the LWJGL natives for the user's OS and architecture.
     */
    fun getCurrentNatives(): String {
        val osName = System.getProperty("os.name")
        val osArch = System.getProperty("os.arch")
        return when {
            osName.startsWith("Free") -> {
                FREE_BSD
            }

            osName.startsWith("Linux") -> {
                if (osArch.isARM()) {
                    if (osArch.is64Bit()) LINUX_ARM64
                    else LINUX_ARM32
                } else {
                    LINUX_X64
                }
            }

            osName.startsWith("Mac") -> {
                if (osArch.isARM()) MAC_ARM64
                else MAC_X64
            }

            osName.startsWith("Windows") -> {
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
}

private fun String.is64Bit(): Boolean {
    return this.contains("64") || this.startsWith("armv8")
}

private fun String.isARM(): Boolean {
    return this.startsWith("arm") || this.startsWith("aarch64")
}