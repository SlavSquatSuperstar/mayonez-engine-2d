plugins {
    id("application") // For creating runnable programs
    id("com.github.johnrengelman.shadow")

    id("org.jetbrains.dokka")
}

description = "The testbed project for Mayonez Engine that contains all the demo scenes."

dependencies {
    implementation(project(":mayonez-base"))
}

// Plugins

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

application { // For running project
    mainModule.set("mayonez.demos")
    mainClass.set("slavsquatsuperstar.demos.DemosLauncher")
    if (isMacOS) {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread") // For LWJGL on macOS
    }
}

tasks {
    shadowJar { // For building fat jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier.set("")
    }
}