plugins {
    id("mayonez.java-conventions")
    id("application") // Enable runnable JVM project

    id(dokkaPlugin)
}

description = "The testbed project for Mayonez Engine that contains all the demo scenes."

dependencies {
    implementation(project(":mayonez-base"))
}

// Plugins and Tasks

application {
    // Leave mainModule blank to prevent ModuleResolutionError
    mainClass = mainClassName
    applicationDefaultJvmArgs = jvmArgs

    // Default LWJGL natives for run
    setNatives(Natives.getDefaultNatives(), "runtimeOnlyDefault")
}

tasks {
    jar {
        useJarDefaults()
        from(configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) })
    }

    // Register the jar tasks

    val packagePlatforms = mapOf<String, String>(
        Natives.MAC_OS_X64 to "Mac",
        Natives.WINDOWS_X64 to "Windows",
        Natives.LINUX_X64 to "Linux"
    )

    packagePlatforms.forEach { natives, platform ->
        register<Jar>("jar$platform") {
            group = "Packaging"
            description = "Copies the release assets for ${platform}."
            configureJarTask(natives, platform)
        }
    }

    // Copy user preference files
    register<Copy>("copyDefaultPreferences") {
        group = "Packaging"
        description = "Copy default config files to the subproject directory, if not present."

        from("../release-assets/resources")
        into("./")
        include("*.json")
        eachFile { if (relativePath.getFile(destinationDir).exists()) exclude() }
        outputs.upToDateWhen { false }
    }

    // Force copy user preference files
    register<Copy>("resetDefaultPreferences") {
        group = "Packaging"
        description = "Overwrite default config files to the subproject directory."

        from("../release-assets/resources")
        into("./")
        include("*.json")
        outputs.upToDateWhen { false }
    }

    compileJava {
        dependsOn("copyDefaultPreferences")
    }

    processResources {
        dependsOn("copyDefaultPreferences")
    }
}

/**
 * Configure the jar task for a specific OS.
 */
fun Jar.configureJarTask(natives: String, platform: String) {
    group = "Packaging"
    description = "Builds the release jar for ${platform}."

    // Set dependencies to include in fat jar
    useJarDefaults()

    setNatives(natives, "runtimeOnly$platform")
    from(sourceSets["main"].output)
    from(configurations["runtimeOnly$platform"]
        .map { if (it.isDirectory) it else zipTree(it) })

    // Don't copy default natives
    from(configurations.runtimeClasspath.get()
        .filter { !it.name.contains("natives") }
        .map { if (it.isDirectory) it else zipTree(it) })

    // Move it to the dist directory
    destinationDirectory = file("../dist/${platform.lowercase()}")
}

/**
 * Set the default parameters to use for every jar task.
 */
fun Jar.useJarDefaults() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(mapOf("Main-Class" to mainClassName))
    }
}

/**
 * Set the native libraries to include in a fat jar.
 */
fun setNatives(natives: String, configuration: String) {
    val config = configurations.create(configuration)
    dependencies {
        config(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
        config("org.lwjgl:lwjgl::$natives")
        config("org.lwjgl:lwjgl-glfw::$natives")
        config("org.lwjgl:lwjgl-opengl::$natives")
        config("org.lwjgl:lwjgl-stb::$natives")
    }
}
