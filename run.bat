@rem Navigate to the project directory
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit

@rem Build the application for Windows
echo "Building mayonez-engine..."
./gradlew clean build

@rem Run the compiled jar file
java -jar build\libs\*.jar