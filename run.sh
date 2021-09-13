#!/bin/sh

SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit
./gradlew clean build
java -XstartOnFirstThread -jar build/libs/*.jar