/**
 * Get the LWJGL natives for the user's OS and architecture.
 *
 * Source: [LWJGL](https://www.lwjgl.org/customize) -> download
 * build.gradle(.kts)
 */
val lwjglNatives: String = getNatives()

private fun getNatives(): String {
    val osName = System.getProperty("os.name")
    val osArch = System.getProperty("os.arch")
    return when {
        osName.startsWith("Linux") -> {
            if (osArch.isARM()) {
                if (osArch.contains("64") || osArch.startsWith("armv8")) "natives-linux-arm64" // Linux 64-bit ARM
                else "natives-linux-arm32" // Linux 32-bit ARM
            } else {
                "natives-linux" // Linux x86
            }
        }

        osName.startsWith("Mac") -> {
            if (osArch.isARM()) "natives-macos-arm64" // macOS Apple Silicon
            else "natives-macos" // macOS Intel
        }

        osName.startsWith("Windows") -> {
            if (osArch.contains("64")) {
                if (osArch.isARM()) "natives-windows-arm64" // Windows 64-bit ARM
                else "natives-windows" // Windows 64-bit
            } else {
                "natives-windows-x86" // Windows 32-bit
            }
        }

        else -> "natives-linux"
    }
}

private fun String.isARM(): Boolean {
    return this.startsWith("arm") || this.startsWith("aarch64")
}