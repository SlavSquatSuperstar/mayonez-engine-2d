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

    /*
     * Package the release for all operating systems.
     */
    register<Copy>("packageAll") {
        dependsOn("packageMac")
        dependsOn("packageWindows")
        dependsOn("packageLinux")
    }

    /*
     * Package the release for macOS.
     */
    register<Copy>("packageMac") {
        dependsOn(":mayonez-demos:jar")

        // Copy jar and resources
        from(project(":mayonez-demos").tasks.named("jar").map { it.outputs })
        from("./LICENSE.txt", "./release-assets/resources", "./release-assets/mac")
        into("./dist/mac")
        include("*.jar", "*.json", "*.txt", "run*")
    }

    /*
     * Package the release for Windows.
     */
    register<Copy>("packageWindows") {
        dependsOn(":mayonez-demos:jar")

        // Copy jar and resources
        from(project(":mayonez-demos").tasks.named("jar").map { it.outputs })
        from("./LICENSE.txt", "./release-assets/resources", "./release-assets/windows")
        into("./dist/windows")
        include("*.jar", "*.json", "*.txt", "run*")
    }

    /*
     * Package the release for Linux.
     */
    register<Copy>("packageLinux") {
        dependsOn(":mayonez-demos:jar")

        // Copy jar and resources
        from(project(":mayonez-demos").tasks.named("jar").map { it.outputs })
        from("./LICENSE.txt", "./release-assets/resources", "./release-assets/linux")
        into("./dist/linux")
        include("*.jar", "*.json", "*.txt", "run*")
    }
}
}
