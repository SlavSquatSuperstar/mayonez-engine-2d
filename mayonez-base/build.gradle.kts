import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("java-library")

    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlin.jvm")
}

description = "The library project for Mayonez Engine that contains the core classes and unit tests."

apply(from = "./get-natives.gradle.kts")

private val lwjglNatives = extensions["lwjglNatives"] as String
private val junitVersion = "5.10.0"

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("org.json:json:20230618")

    // LWJGL Modules
    implementation("org.joml:joml:1.10.5")

    implementation(platform("org.lwjgl:lwjgl-bom:3.3.2")) // Bill of materials: set version for all libs
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    implementation("org.lwjgl:lwjgl::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    implementation("org.lwjgl:lwjgl-stb::$lwjglNatives")

    // Test Libraries
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.4.0")
}

// Plugins and Tasks

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks {
    compileJava {
        dependsOn(compileKotlin)
    }

    compileKotlin {
        compilerOptions {
            suppressWarnings = true
            destinationDirectory.set(file("build/classes/java/main"))
        }
    }

    test { useJUnitPlatform() }
}
