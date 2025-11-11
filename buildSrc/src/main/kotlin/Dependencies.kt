// Languages

/** The default JDK version for the toolchain. */
const val javaVersion: Int = 21

// Libraries

/** The default Kotlin version for libraries and plugins. */
const val kotlinVersion: String = "2.2.20"

/** The version for the JUnit testing libraries. */
const val junitVersion: String = "6.0.0"

/** The version for the LWJGL framework. */
const val lwjglVersion: String = "3.3.4"

/** The LWJGL natives for the current OS and architecture. */
val lwjglNatives: String = Natives.getDefaultNatives()

// Plugins

/**
 * The Dokka plugin for creating Kotlin documentation, must be applied
 * individually to all projects.
 */
const val dokkaPlugin: String = "org.jetbrains.dokka"

/** The Kotlin plugin for compiling Kotlin files to the JVM. */
const val kotlinPlugin: String = "org.jetbrains.kotlin.jvm"
