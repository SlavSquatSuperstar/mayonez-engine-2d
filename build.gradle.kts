plugins {
    id("mayonez.java-conventions")

    id(kotlinPlugin) version kotlinVersion apply false
    id(dokkaPlugin) version "1.9.20" apply true
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

allprojects {
    group = "slavsquatsuperstar"
    version = "0.8.1-pre2-snapshot"
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
        enabled = false // Build the runnable jar in demos instead
    }

    // Register the package tasks

    val packagePlatforms = mapOf<String, String?>(
        "Mac" to "macOS", "Windows" to null, "Linux" to null
    )

    register("packageAll") {
        description = "Packages the release for all operating systems."
        packagePlatforms.keys.forEach { dependsOn("package$it") }
    }

    packagePlatforms.forEach { platform, platformDesc ->
        registerCopyZipTasks(platform, platformDesc = platformDesc)
    }
}

/**
 * Register the copy and zip tasks for a specific OS.
 */
fun TaskContainer.registerCopyZipTasks(platform: String, platformDesc: String? = null) {
    register<Copy>("copy$platform") {
        group = "Packaging"
        description = "Copies the release assets for ${platformDesc ?: platform}."
        configureCopyTask(platform)
    }

    register<Zip>("zip$platform") {
        group = "Packaging"
        description = "Zips the release assets for ${platformDesc ?: platform}."
        configureZipTask(platform)
    }

    register("package$platform") {
        group = "Packaging"
        description = "Copies and zips the release assets for ${platformDesc ?: platform}."
        dependsOn("copy$platform")
        dependsOn("zip$platform")
    }
}

/**
 * Configure the copy task for a specific OS.
 */
fun Copy.configureCopyTask(platform: String) {
    // Copy OS-specific resources
    dependsOn(project(":mayonez-demos").tasks.named("jar$platform"))
    from("./LICENSE.txt", "./release-assets/resources", "./release-assets/${platform.lowercase()}")
    into("./dist/${platform.lowercase()}")
    include("*.json", "*.txt", "run*")
}

/**
 * Configure the zip task for a specific OS.
 */
fun Zip.configureZipTask(platform: String) {
    // Zip copied files
    from(tasks.named("copy$platform")
        .map { it.outputs })
    include("*.jar", "*.json", "*.txt", "run*")
    destinationDirectory = file("./dist")
    archiveFileName = "mayonez-engine-${project.version}-${platform.lowercase()}.zip"
}
