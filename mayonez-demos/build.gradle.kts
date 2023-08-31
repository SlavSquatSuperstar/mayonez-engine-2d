plugins {
    id("application") // For creating runnable programs
    id("com.github.johnrengelman.shadow") version "8.1.1" // For making fat jars

    id("org.jetbrains.dokka")
}

description = "The testbed project for Mayonez Engine that contains all the demo scenes."

dependencies {
    implementation(project(":mayonez-base"))
}

// Detect User OS
private val osName = System.getProperty("os.name")
private var macOS = osName.startsWith("Mac")

// Plugins

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application { // For running project
    mainModule.set("mayonez.demos")
    mainClass.set("slavsquatsuperstar.demos.DemosLauncher")
    if (macOS) {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread") // For LWJGL on macOS
    }
}

tasks {
    shadowJar { // For building fat jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier = ""
    }
}