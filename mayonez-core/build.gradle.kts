plugins {
    id("mayonez.library-conventions")
    id("mayonez.testing-conventions")

    id(shadowPlugin)
    id(dokkaPlugin)
    id(kotlinPlugin)
}

description = "The core project for Mayonez Engine that contains the base classes."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:20230618")
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
