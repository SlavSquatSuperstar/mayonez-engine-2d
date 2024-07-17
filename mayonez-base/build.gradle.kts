import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("mayonez.library-conventions")

    id(kotlinPlugin)
    id(dokkaPlugin)
}

description = "The core library for Mayonez Engine that contains the API classes."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.joml:joml:1.10.6")

    // LWJGL Modules
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
    compileJava {
        dependsOn("copyKotlinClasses") // Compile Kotlin sources before Java sources
    }

    withType<KotlinCompile> {
        compilerOptions {
            suppressWarnings.set(true)
        }
    }

    // Copy outputs into java build folder
    register<Copy>("copyKotlinClasses") {
        dependsOn(compileKotlin) // Compile Kotlin sources before Java sources
        from("build/classes/kotlin/main")
        into("build/classes/java/main")
        include("**/*.class", "**/*.kotlin_module")
        doLast {
            delete("build/classes/java/main/module-info.class") // Mark compileJava as out-of-date
        }
    }
}
