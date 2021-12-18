#!/bin/sh
# Builds and runs the application on macOS
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit
./gradlew clean build
# LWJGL requires a special argument
java -XstartOnFirstThread -jar build/libs/*.jar