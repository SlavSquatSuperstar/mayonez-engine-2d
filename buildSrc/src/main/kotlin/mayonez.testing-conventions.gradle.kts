// Plugin for creating testable projects
plugins {
    id("mayonez.java-conventions")

    id("java")
}

private val junitVersion = "5.10.0"

dependencies {
    // Add Test Libraries
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.4.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}