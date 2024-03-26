# Script Name: package
# Purpose:     Packages the Mayonez Engine program for release on GitHub.
# Usage:       .\package.ps1
# Author:      SlavSquatSuperstar

# Get the original calling directory
$Location = (Get-Location).Path

# Navigate to the project directory
$SCRIPT_DIR = $PSSCRIPTROOT
Set-Location "$SCRIPT_DIR"
if (-not$?)
{
  exit
}

## Functions ##

# Exit and navigate to the previous directory
function Exit-Script {
  param (
    $ExitCode
  )
  Set-Location $Location
  exit $ExitCode
}

# Build the Gradle project
Write-Output "Building Mayonez Engine..."
.\gradlew.bat jar
if (-not$?)
{
  Write-Output "Error building Mayonez Engine."
  Exit-Script 1
} else
{
  Write-Output "Successfully built Mayonez Engine."
}

# Replace the compiled .jar file
Remove-Item dist\mayonez*.jar
Copy-Item mayonez-demos\build\libs\mayonez*.jar dist\

# Copy resource files
Copy-Item LICENSE.txt dist\
Copy-Item mayonez-demos\*.json dist\

# Zip the dist folder
Set-Location dist
if (-not$?)
{
  exit
}
Remove-Item mayonez*.zip
Compress-Archive -Path mayonez*.jar, run*, preferences.json, *.txt -DestinationPath mayonez-engine.zip

# Show success message
Write-Output "Successfully packaged Mayonez Engine."
Exit-Script 0
