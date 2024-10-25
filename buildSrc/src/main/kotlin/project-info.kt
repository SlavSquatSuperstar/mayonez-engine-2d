/** The default JDK version for the toolchain. */
const val javaVersion: Int = 21

/** The main module for the .jar file. */
const val mainModuleName: String = "mayonez.demos"

/** The main class for the .jar file. */
const val mainClassName: String = "slavsquatsuperstar.demos.DemosLauncher"

/** The default JVM args for running the .jar file. */
val jvmArgs: List<String>
    get() {
        return if (isMacOS()) listOf("-XstartOnFirstThread") // For LWJGL on macOS
        else emptyList()
    }

/** Whether the user is running macOS (for LWJGL VM args). */
private fun isMacOS(): Boolean {
    val osName = System.getProperty("os.name")
    return osName.startsWith("Mac")
}