import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("mayonez.library-conventions")

    id(kotlinPlugin)
    id(dokkaPlugin)
    id(errorPronePlugin)
    id(nullAwayPlugin)
}

description = "The core library for Mayonez Engine that contains the API classes."

dependencies {
    // Code Dependencies
    implementation("commons-cli:commons-cli:1.11.0")
    implementation("org.apache.commons:commons-csv:1.14.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.joml:joml:1.10.8")
    implementation("org.json:json:20250107")

    // LWJGL Modules
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")

    // Static Analysis
    errorprone("com.google.errorprone:error_prone_core:2.49.0")
    errorprone("com.uber.nullaway:nullaway:$nullAwayVersion")
    compileOnly("com.uber.nullaway:nullaway-annotations:$nullAwayVersion")
    compileOnly("org.jspecify:jspecify:1.0.0")
}

// Plugins and Tasks

tasks {
    compileKotlin {
        compilerOptions {
            suppressWarnings.set(true)
        }
        doLast {
            // Always recompile Java after compiling Kotlin
            compileJava.get().outputs.upToDateWhen { false }
        }
    }

    // Copy Kotlin outputs into Java build folder
    register<Copy>("copyKotlinClasses") {
        dependsOn(compileKotlin)
        from("build/classes/kotlin/main")
        into("build/classes/java/main")
        include("**/*.class", "**/*.kotlin_module")
    }

    compileJava {
        dependsOn("copyKotlinClasses")
    }

    // Configure Error Prone and NullAway
    withType<JavaCompile> {
        options.errorprone {
            disableAllChecks = true // Only do NullAway checks
            nullaway {
                error() // Set severity level
                onlyNullMarked = true // Only check in null-marked code
                jspecifyMode = true // JSpecify Mode
            }
        }
    }

    // Disable on test code
    compileTestJava {
        options.errorprone {
            enabled = false
        }
    }

}