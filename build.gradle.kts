plugins {
    id("mayonez.java-conventions")

    id(kotlinPlugin) version kotlinVersion apply false
    id(dokkaPlugin) version "1.9.20" apply true
}

// Project Info
description = "The root project for Mayonez Engine that contains all modules."

allprojects {
    group = "slavsquatsuperstar"
    version = "0.8.1-pre5"
}

// Subprojects
dependencies {
    implementation(project(":mayonez-base"))
    implementation(project(":mayonez-demos"))
}

// Plugins and Tasks
tasks {
    wrapper {
        gradleVersion = "8.11"
        distributionType = Wrapper.DistributionType.BIN
    }

    jar {
        enabled = false // Build the runnable jar in demos instead
    }

    // Register the package tasks

    val packagePlatforms = mapOf<String, String?>(
        "Mac" to "macOS", "Windows" to null, "Linux" to null
    )

    packagePlatforms.forEach { platform, platformDesc ->
        registerCopyZipTasks(platform, platformDesc = platformDesc)
    }

    register("packageAll") {
        group = "Packaging"
        description = "Packages the release for all operating systems."
        dependsOn("deletePackages")
        packagePlatforms.keys.forEach { dependsOn("package$it") }
    }

    register("deletePackages") {
        group = "Packaging"
        description = "Deletes all outdated package files."

        /*
         * Delete all jar/zip files who do not have filename format
         * <project>-<version>.zip or <project>-<version>-<os>.zip.
         */
        file("./dist/").walk()
            .filter { it.isFile }
            .filter { it.name.contains(Regex("\\.(zip|jar)")) }
            .filter { !it.name.contains(Regex("${project.version}\\.jar")) }
            .filter { !it.name.contains(Regex("${project.version}-[a-z]+\\.zip")) }
            .forEach { delete(it) }
    }
}

/**
 * Register and configure the copy and zip tasks for a specific OS.
 */
fun TaskContainer.registerCopyZipTasks(platform: String, platformDesc: String? = null) {
    register<Copy>("copy$platform") {
        group = "Packaging"
        description = "Copies the release assets for ${platformDesc ?: platform}."

        dependsOn(project(":mayonez-demos").tasks.named("jar$platform"))
        from("./LICENSE.txt", "./release-assets/resources", "./release-assets/${platform.lowercase()}")
        into("./dist/${platform.lowercase()}")
        include("*.json", "*.txt", "run*")
    }

    register<Zip>("zip$platform") {
        group = "Packaging"
        description = "Zips the release assets for ${platformDesc ?: platform}."

        from(named("copy$platform").map { it.outputs })
        include("*.jar", "*.json", "*.txt", "run*")
        destinationDirectory = file("./dist")
        archiveFileName = "mayonez-engine-${project.version}-${platform.lowercase()}.zip"
    }

    register("package$platform") {
        group = "Packaging"
        description = "Copies and zips the release assets for ${platformDesc ?: platform}."
        dependsOn("copy$platform")
        dependsOn("zip$platform")
    }
}
