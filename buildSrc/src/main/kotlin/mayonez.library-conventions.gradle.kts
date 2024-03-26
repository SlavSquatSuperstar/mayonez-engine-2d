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
    testImplementation("org.mockito:mockito-core:5.11.0")
}

// Enable JUnit testing
tasks {
    test {
        useJUnitPlatform()
    }
}