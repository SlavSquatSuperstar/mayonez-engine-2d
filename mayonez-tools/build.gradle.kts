import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("mayonez.library-conventions")

    id(kotlinPlugin)
    id(dokkaPlugin)
}

description = "A helper library containing math and file I/O utilities."

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:20240303")
}

// Plugins and Tasks

tasks {
    compileJava {
        dependsOn("copyKotlinClasses") // Make sure Kotlin classes are in both folders
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
        include("**/*.class", "**/*.kotlin_module")
        into("build/classes/java/main")
    }
}
