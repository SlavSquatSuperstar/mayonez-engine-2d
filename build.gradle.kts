plugins {
    id("mayonez.java-conventions")

    id(kotlinPlugin) version kotlinVersion apply false
    id(dokkaPlugin) version "1.9.20" apply true
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

allprojects {
    group = "slavsquatsuperstar"
    version = "0.8.0-pre12"
}

// Subprojects
dependencies {
    implementation(project(":mayonez-tools"))
    implementation(project(":mayonez-base"))
    implementation(project(":mayonez-demos"))
}

// Plugins and Tasks
tasks {
    wrapper {
        gradleVersion = "8.10.2"
        distributionType = Wrapper.DistributionType.BIN
    }

    jar {
        // Build the runnable jar in demos instead
        enabled = false
    }
}
