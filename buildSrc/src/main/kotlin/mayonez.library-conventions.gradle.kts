import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.*

// Plugin for creating testable library projects
plugins {
    id("mayonez.java-conventions")
    id("java-library")
}

// Add testing libraries
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    // Enable JUnit testing
    test {
        useJUnitPlatform()
    }

    // Don't copy Kotlin module
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}