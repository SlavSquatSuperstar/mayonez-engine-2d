import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("mayonez.library-conventions")

    id(kotlinPlugin)
    id(dokkaPlugin)
}

description = "The library project for Mayonez Engine that contains the core and API classes."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:20240303")

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
}

// Plugins and Tasks

tasks {
    compileJava {
        dependsOn(copyKotlinClasses) // Compile Kotlin sources before Java sources
    }

    withType<KotlinCompile> {
        compilerOptions {
            suppressWarnings.set(true)
        }
    }
}

// Copy outputs into java build folder
val copyKotlinClasses = tasks.register<Copy>("copyKotlinClasses") {
    dependsOn(tasks.compileKotlin) // Compile Kotlin sources before Java sources
    from("build/classes/kotlin/main")
    into("build/classes/java/main")
    include("**/*.class", "**/*.kotlin_module")
    doLast {
        delete("build/classes/java/main/module-info.class")
    }
}
