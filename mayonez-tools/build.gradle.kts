plugins {
    id("mayonez.library-conventions")

    id(kotlinPlugin)
    id(dokkaPlugin)
}

description = "A helper library containing math and file I/O utilities."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:20231013")
}

// Plugins and Tasks

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE // Don't copy Kotlin module
    }

    testClasses {
        dependsOn(classes) // Compile main sources before test sources
    }

    compileTestJava {
        dependsOn(compileTestKotlin) // Compile Kotlin sources before Java sources
    }

    compileTestKotlin {
        compilerOptions {
            suppressWarnings.set(true)
        }
    }

    compileJava {
        dependsOn(compileKotlin) // Compile Kotlin sources before Java sources
    }

    compileKotlin {
        compilerOptions {
            suppressWarnings.set(true)
        }
    }
}
