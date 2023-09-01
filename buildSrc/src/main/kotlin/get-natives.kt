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
            if (osArch.startsWith("arm") || osArch.startsWith("aarch64")) {
                if (osArch.contains("64") || osArch.startsWith("armv8")) "natives-linux-arm64"
                else "natives-linux-arm32"
            } else {
                "natives-linux"
            }
        }

        osName.startsWith("Mac") -> {
            if (osArch.contains("arm")) "natives-macos-arm64"
            else "natives-macos"
        }

        osName.startsWith("Windows") -> {
            if (osArch.contains("64")) {
                if (osArch.startsWith("aarch64")) "natives-windows-arm64"
                else "natives-windows"
            } else {
                "natives-windows-x86"
            }
        }

        else -> "natives-linux"
    }
}