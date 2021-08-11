#!/bin/sh

BASEDIR=$(dirname "$0")
cd $BASEDIR
./gradlew clean build
java -jar build/libs/*.jar