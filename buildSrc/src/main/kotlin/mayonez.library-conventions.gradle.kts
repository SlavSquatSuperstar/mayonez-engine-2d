import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.test
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.testImplementation
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.testRuntimeOnly

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

// Enable JUnit testing
tasks {
    test {
        useJUnitPlatform()
    }
}