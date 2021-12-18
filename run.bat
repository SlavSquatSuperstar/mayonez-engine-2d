@rem Builds and runs the application on Windows
@rem I have no idea if this works lol

SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit
./gradlew clean build
java -jar build/libs/*.jar