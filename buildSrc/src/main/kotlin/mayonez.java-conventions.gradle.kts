// Base plugin for all JVM projects
plugins {
    id("java")
}

// Set a common JDK version
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

// Enable Maven repository
repositories {
    mavenCentral()
}