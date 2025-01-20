plugins {
    id("mayonez.library-conventions")

    id(kotlinPlugin)
    id(dokkaPlugin)
}

description = "The core library for Mayonez Engine that contains the API classes."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:20250107")
    implementation("org.joml:joml:1.10.8")

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

}
