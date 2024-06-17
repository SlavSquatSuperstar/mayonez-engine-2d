plugins {
    id("mayonez.java-conventions")

    id(shadowPlugin) version "8.1.1" apply false
    id(kotlinPlugin) version kotlinVersion apply false
    id(dokkaPlugin) version "1.9.20" apply true
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

allprojects {
    group = "slavsquatsuperstar"
    version = "0.8.0-pre9"
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
        gradleVersion = "8.8"
        distributionType = Wrapper.DistributionType.BIN
    }

    jar {
        enabled = false
    }
}
