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
//    mainModule = mainModuleName // Leave blank to prevent module ResolutionError
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
}
