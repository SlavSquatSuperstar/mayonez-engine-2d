plugins {
    id("java")

    id("org.jetbrains.dokka")
}

description = "The testbed project for Mayonez Engine that contains all the demo scenes."

dependencies {
    implementation(project(":mayonez-base"))
}

// Plugins

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}