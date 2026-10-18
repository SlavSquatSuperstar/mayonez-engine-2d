/*
 * Jar manifest attributes and JVM parameters.
 *
 * Note that some symbols may appear unused since build.gradle scripts use
 * the compiled version of this file.
 */

/** The main module for the .jar file. */
const val mainModuleName: String = "mayonez.demos"

/** The main class for the .jar file. */
const val mainClassName: String = "slavsquatsuperstar.demos.DemosLauncher"

/** The default JVM args for running the .jar file. */
val defaultJvmArgs: List<String> =
    if (Natives.isMacOS()) listOf("-XstartOnFirstThread")
    else emptyList()

val packagePlatforms: Map<String, String> = mapOf(
    "Mac" to "macOS", "Windows" to "Windows", "Linux" to "Linux"
)