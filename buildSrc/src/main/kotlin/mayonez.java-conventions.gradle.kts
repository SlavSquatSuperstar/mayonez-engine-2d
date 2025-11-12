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

// Aggregate Dokka Outputs
//dokka {
//    dokkaPublications.javadoc {
//        outputDirectory.set(
//            rootProject.layout.buildDirectory
//                .dir("dokka/javadoc/${project.name}")
//        )
//    }
//}

// Enable Maven repository
repositories {
    mavenCentral()
}