/** The main module for the .jar file. */
const val mainModuleName: String = "mayonez.demos"

/** The main class for the .jar file. */
const val mainClassName: String = "slavsquatsuperstar.demos.DemosLauncher"

/** The LWJGL natives for the current OS and architecture. */
val lwjglNatives: String = Natives.getDefaultNatives()

/** The default JVM args for running the .jar file. */
val jvmArgs: List<String>
    get() {
        return if (Natives.isMacOS()) listOf("-XstartOnFirstThread")
        else emptyList()
    }
