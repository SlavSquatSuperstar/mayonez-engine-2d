plugins {
    id("mayonez.application-conventions")

    id(shadowPlugin)
    id(dokkaPlugin)
}

description = "The testbed project for Mayonez Engine that contains all the demo scenes."

dependencies {
    implementation(project(":mayonez-base"))
}

// Plugins and Tasks

application {
    mainModule.set(mainModuleName)
    mainClass.set(mainClassName)
}

tasks {
    shadowJar { // For building fat jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier.set("")
    }
}