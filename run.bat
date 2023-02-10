@echo off
@rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit 1

@rem arguments
IF "%1" == "false" (
    set USE_GL=false
) ELSE (
    set USE_GL=true
)

@rem Check if Java is installed
java --version
IF "%ERRORLEVEL%"==0 (
    echo "Java is installed."
) ELSE (
    echo "Java is not installed."
    exit 1
)

@rem Build the application for Windows
echo "Building mayonez-engine..."
.\gradlew.bat clean build

@rem Run the compiled jar file
java -jar build\libs\*.jar "%USE_GL%"
