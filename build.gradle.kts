plugins {
    id("mayonez.java-conventions")

    id(shadowPlugin) version "8.1.1" apply false
    id(dokkaPlugin) version "1.8.20" apply true
    id(kotlinPlugin) version kotlinVersion apply false
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

allprojects {
    group = "slavsquatsuperstar"
    version = "0.8.0-pre2-snapshot"
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
        gradleVersion = "8.5"
        distributionType = Wrapper.DistributionType.BIN
    }
}