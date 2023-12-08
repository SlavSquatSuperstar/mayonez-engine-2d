plugins {
    id("mayonez.library-conventions")
    id("mayonez.testing-conventions")

    id(shadowPlugin)
    id(dokkaPlugin)
    id(kotlinPlugin)
}

description = "A helper library containing math and file I/O tools."

dependencies {
    // Code Dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:20231013")
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
