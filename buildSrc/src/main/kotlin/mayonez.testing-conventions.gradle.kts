// Plugin for creating testable projects
plugins {
    id("mayonez.java-conventions")

    id("java")
}

dependencies {
    // Add Test Libraries
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    test {
        useJUnitPlatform()
    }
}