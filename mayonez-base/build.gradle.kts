plugins {
    id("mayonez.library-conventions")

    id(shadowPlugin)
    id(dokkaPlugin)
    id(kotlinPlugin)
}

description = "The core library for Mayonez Engine that contains the API classes."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    // LWJGL Modules
    implementation("org.joml:joml:1.10.5")
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion")) // Bill of materials: set version for all libs

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    implementation("org.lwjgl:lwjgl::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-stb::$lwjglNatives")

    // Subprojects
    api(project(":mayonez-tools"))
}

// Plugins and Tasks

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier = ""
    }

    compileJava {
        dependsOn(compileKotlin)
    }

    compileKotlin {
        compilerOptions {
            suppressWarnings.set(true)
            destinationDirectory = file("build/classes/java/main")
        }
    }
}
