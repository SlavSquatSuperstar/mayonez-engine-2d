plugins {
    id("java")

    id("com.github.johnrengelman.shadow") version "8.1.1" apply false // For making fat jars
    id("org.jetbrains.dokka") version "1.8.20" // For Kotlin documentation, must apply individually
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false // For compiling Kotlin files
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

// Project Settings
allprojects {
    // Name
    group = "slavsquatsuperstar"
    version = "0.7.10-pre3-snapshot"

    // Dependencies
    repositories { mavenCentral() }
}

dependencies {
    // Subprojects
    implementation(project(":mayonez-base"))
    implementation(project(":mayonez-demos"))
}

// Plugins and Tasks

tasks {
    wrapper {
        gradleVersion = "8.3"
        distributionType = Wrapper.DistributionType.BIN
    }
}