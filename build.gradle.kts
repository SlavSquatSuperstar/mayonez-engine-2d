plugins {
    id("java")

    id(shadowPlugin) version "8.1.1" apply false
    id(dokkaPlugin) version "1.8.20" apply true
    id(kotlinPlugin) version kotlinVersion apply false
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