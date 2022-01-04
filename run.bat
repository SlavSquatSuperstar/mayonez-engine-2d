@rem Build the application for Windows
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit
./gradlew clean build

@rem Run the created jar file
java -jar build\libs\*.jar