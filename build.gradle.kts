plugins {
    id("application") // For creating runnable programs

    id("com.github.johnrengelman.shadow") version "8.1.1" // For making fat jars
    id("org.jetbrains.dokka") version "1.8.20" // For Kotlin documentation, must apply individually
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false // For compiling Kotlin files
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."
extensions.add("javaVersion", 17)

// Detect User OS
private val osName = System.getProperty("os.name")
private var macOS = osName.startsWith("Mac")

// Project Settings
allprojects {
    // Name
    group = "slavsquatsuperstar"
    version = "0.7.10-pre2"

    // Dependencies
    repositories { mavenCentral() }
}

dependencies {
    // Subprojects
    implementation(project(":mayonez-base"))
    implementation(project(":mayonez-demos"))
}

// Plugins and Tasks

application { // For running project
    mainModule.set("mayonez.demos")
    mainClass.set("slavsquatsuperstar.demos.DemosLauncher")
    if (macOS) {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread") // For LWJGL on macOS
    }
}

tasks {
    wrapper {
        gradleVersion = "8.3"
        distributionType = Wrapper.DistributionType.BIN
    }

    shadowJar { // For building fat jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier = ""
    }
}