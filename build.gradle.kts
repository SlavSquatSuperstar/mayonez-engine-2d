plugins {
    id("mayonez.java-conventions")

    id(kotlinPlugin) version kotlinVersion apply false
    id(dokkaPlugin) version "1.9.20" apply true
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

allprojects {
    group = "slavsquatsuperstar"
    version = "0.8.1-pre1-snapshot"
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

    register<Copy>("packageAll") {
        description = "Packages the release for all operating systems."
        dependsOn("packageMac")
        dependsOn("packageWindows")
        dependsOn("packageLinux")
    }

    register<Copy>("packageMac") {
        description = "Packages the release for macOS."
        configurePackageTask("mac")
    }

    register<Copy>("packageWindows") {
        description = "Packages the release for Windows."
        configurePackageTask("windows")
    }

    register<Copy>("packageLinux") {
        description = "Packages the release for Linux."
        configurePackageTask("linux")
    }
}

/**
 * Configure the package task for a specific OS.
 */
fun Copy.configurePackageTask(platform: String) {
    dependsOn(":mayonez-demos:jar")

    // Copy jar and OS-specific resources
    from(project(":mayonez-demos").tasks.named("jar").map { it.outputs })
    from("./LICENSE.txt", "./release-assets/resources", "./release-assets/$platform")
    into("./dist/$platform")
    include("*.jar", "*.json", "*.txt", "run*")
}