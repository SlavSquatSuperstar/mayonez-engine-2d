#!/bin/sh
# Build the application for macOS/Linux
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit
./gradlew clean build

# Run the created jar file
# LWJGL requires a special argument on macOS
java -XstartOnFirstThread -jar build/libs/*.jar