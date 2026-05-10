// Languages

/** The default JDK version for the toolchain. */
const val javaVersion: Int = 21

// Libraries

/** The default Kotlin version for libraries and plugins. */
const val kotlinVersion: String = "2.2.20"

/** The version for the JUnit testing libraries. */
const val junitVersion: String = "6.0.0"

/** The version for the LWJGL framework. */
const val lwjglVersion: String = "3.3.6"

/** The LWJGL natives for the current OS and architecture. */
val lwjglNatives: String = Natives.getDefaultNatives()

/** The version for the NullAway libraries. */
const val nullAwayVersion: String = "0.13.4"

// Plugins

/** The Dokka plugin for creating Kotlin documentation. */
const val dokkaPlugin: String = "org.jetbrains.dokka"
// Javadoc plugin does not correctly aggregate, so use HTML for now

/** The Kotlin plugin for compiling Kotlin files to the JVM. */
const val kotlinPlugin: String = "org.jetbrains.kotlin.jvm"

/** The Error Prone plugin for compile-time static analysis. */
const val errorPronePlugin: String = "net.ltgt.errorprone"

/** The NullAway plugin for analyzing nullability annotations. */
const val nullAwayPlugin: String = "net.ltgt.nullaway"
