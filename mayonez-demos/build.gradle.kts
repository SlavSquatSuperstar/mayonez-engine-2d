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
    // mainModule = mainModuleName // Leave blank to prevent module ResolutionError
    mainClass = mainClassName
    applicationDefaultJvmArgs = jvmArgs
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes(mapOf("Main-Class" to mainClassName))
        }

        // Build a fatjar with all the dependencies
        from(configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) })
    }

    withType<ProcessResources> {
        dependsOn("copyDefaultPreferences")
    }

    withType<JavaCompile> {
        dependsOn("copyDefaultPreferences")
    }

    /*
     * Copy default config files if not present in this directory.
     * Sources:
     * - https://discuss.gradle.org/t/gradle-copy-task-dont-overrite-uptodatewhen/26785/2
     * - https://docs.gradle.org/current/userguide/working_with_files.html
     */
    register<Copy>("copyDefaultPreferences") {
        println("Copied preferences")
        from("../release-assets/resources")
        into("./")
        include("*.json")
        eachFile { if (relativePath.getFile(destinationDir).exists()) exclude() }
    }
}
