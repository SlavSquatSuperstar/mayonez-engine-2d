// Plugin for creating testable library projects
plugins {
    id("mayonez.java-conventions")
    id("java-library")
}

// Add testing libraries
dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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