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

    // TODO resolves dependencies twice
    setNatives(Natives.getCurrentNatives()) // Default LWJGL natives for run
}

tasks {
    jar {
        useJarDefaults()
    }

    register<Jar>("jarMac") {
        configureJarTask(Natives.MAC_OS_X64, "mac")
    }

    register<Jar>("jarWindows") {
        configureJarTask(Natives.WINDOWS_X64, "windows")
    }

    register<Jar>("jarLinux") {
        configureJarTask(Natives.LINUX_X64, "linux")
    }

    compileJava {
        dependsOn("copyDefaultPreferences")
    }

    processResources {
        dependsOn("copyDefaultPreferences")
    }

    // Source: https://discuss.gradle.org/t/gradle-copy-task-dont-overrite-uptodatewhen/26785/2
    register<Copy>("copyDefaultPreferences") {
        group = "Custom"
        description = "Copy default config files to this directory, if not present."

        from("../release-assets/resources")
        into("./")
        include("*.json")
        eachFile { if (relativePath.getFile(destinationDir).exists()) exclude() }
    }
}

fun Jar.configureJarTask(natives: String, platform: String) {
    // Set the natives to use for compilation
    setNatives(natives)

    // Set dependencies to include in fat jar
    useJarDefaults()
    from(tasks.compileJava)
    from(tasks.processResources)

    // Move it to the dist directory
    destinationDirectory = file("../dist/$platform")
}

fun Jar.useJarDefaults() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(mapOf("Main-Class" to mainClassName))
    }

    from(configurations.runtimeClasspath.get()
        .map { if (it.isDirectory) it else zipTree(it) })
}

fun setNatives(natives: String) {
    dependencies {
        println("demos natives = $natives")
        runtimeOnly("org.lwjgl:lwjgl::$natives")
        runtimeOnly("org.lwjgl:lwjgl-glfw::$natives")
        runtimeOnly("org.lwjgl:lwjgl-opengl::$natives")
        runtimeOnly("org.lwjgl:lwjgl-stb::$natives")
    }
}
