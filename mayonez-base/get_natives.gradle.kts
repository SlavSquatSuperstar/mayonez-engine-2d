// Configure LWJGL natives
// Source: https://www.lwjgl.org/customize -> download build.gradle(.kts)
private val osName = System.getProperty("os.name")
private val osArch = System.getProperty("os.arch")
private var lwjglNatives = "natives-linux"

when {
    osName.startsWith("Linux") -> {
        lwjglNatives = if (osArch.startsWith("arm") || osArch.startsWith("aarch64")) {
            if (osArch.contains("64") || osArch.startsWith("armv8")) "natives-linux-arm64"
            else "natives-linux-arm32"
        } else {
            "natives-linux"
        }
    }

    osName.startsWith("Mac") -> {
        lwjglNatives = if (osArch.contains("arm")) "natives-macos-arm64"
        else "natives-macos"
    }

    osName.startsWith("Windows") -> {
        lwjglNatives = if (osArch.contains("64")) {
            if (osArch.startsWith("aarch64")) "natives-windows-arm64"
            else "natives-windows"
        } else {
            "natives-windows-x86"
        }
    }
}

extensions.add("lwjglNatives", lwjglNatives)