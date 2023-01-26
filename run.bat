@rem Navigate to the project directory
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR" || exit

@rem Check if Java is installed
java --version
IF %ERRORLEVEL% == 0 (
echo "Java is installed."
) ELSE (
    echo "Java is installed."
    exit 1
)

@rem Build the application for Windows
echo "Building mayonez-engine..."
.\gradlew clean build

@rem Run the compiled jar file
java -jar build\libs\*.jar "true"
