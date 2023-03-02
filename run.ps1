# Navigate to the project directory
$SCRIPT_DIR = $PSSCRIPTROOT
cd $SCRIPT_DIR || exit 1

# Get user arguments
if ("%1" -eq "false")
{
    $USE_GL = $FALSE
}
else
{
    $USE_GL = $TRUE
}

# Check if Java is installed
java --version
if (-not$?)
{
    echo "Java is installed."
}
else
{
    echo "Java is not installed."
    exit 1
}

# Build the application for Windows
echo "Building mayonez-engine..."
.\gradlew.bat clean build

# Run the compiled jar file
if ($USE_GL)
{
    echo "Launching with OpenGL Engine."
}
else
{
    echo "Launching with AWT Engine."
}
java -jar build\libs\*.jar "$USE_GL"