plugins {
    id("mayonez.java-conventions")
    id("application") // Enable runnable JVM project

    id(shadowPlugin)
    id(dokkaPlugin)
}

description = "The testbed project for Mayonez Engine that contains all the demo scenes."

dependencies {
    implementation(project(":mayonez-base"))
}

// Plugins and Tasks

application {
    mainModule = mainModuleName
    mainClass = mainClassName
    applicationDefaultJvmArgs = jvmArgs
}

tasks {
    shadowJar { // For building fat jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier = ""
    }
}