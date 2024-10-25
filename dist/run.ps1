# Script Name: run
# Purpose:     Runs the project for Windows users with PowerShell installed.
# Usage:       .\run.ps1 [-h/--help] [-e/--engine gl/awt]"
# Author:      SlavSquatSuperstar

# Get the original calling directory
$Location = (Get-Location).Path

# Navigate to the project directory
$SCRIPT_DIR = $PSSCRIPTROOT
Set-Location $SCRIPT_DIR
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

# Show help and exit with a code
function Show-Help {
    param (
        $ExitCode
    )
    Write-Output "Usage: run [-h/--help] [-e/--engine gl/awt]"
    Exit-Script $ExitCode
}

# Configure the program to run using GL/AWT
function Set-Engine-Type {
    param (
        $EngineType
    )

    switch ($EngineType) {
        "gl" {
            return $true
        }
        "awt" {
            return $false
        }
        "" {
            Write-Output "Option `"--engine`" requires one argument."
            Show-Help 1
        }
        Default {
            Write-Output "Invalid engine type `"$EngineType`"."
            Show-Help 1
        }
    }
}

## Script Start ##

# Initialize variables
$USE_GL = $true

# Get user arguments
for ($i = 0; $i -lt $args.Count; $i++) {
    $arg_i = $args[$i]
    switch ($arg_i) {
        "-h" {
            Show-Help 0
        }
        "--help" {
            Show-Help 0
        }
        "-e" {
            $USE_GL = Set-Engine-Type $args[$i + 1]
            $i++
        }
        "--engine" {
            $USE_GL = Set-Engine-Type $args[$i + 1]
            $i++
        }
        Default {
            Write-Output "Invalid option `"$arg_i`"."
            Show-Help 1
        }
    }
}

# Check if Java is installed
if (java --version)
{
    Write-Output "Java is installed."
}
else
{
    Write-Output "Java is not installed."
    Exit-Script 1
}

$JAR_FILE = ""
foreach ($F in Get-ChildItem mayonez*.jar)
{
    $JAR_FILE = $F
}

# Run the compiled jar file
if ($USE_GL)
{
    Write-Output "Launching with OpenGL Engine."
    java -jar $JAR_FILE --engine gl
}
else
{
    Write-Output "Launching with AWT Engine."
    java -jar $JAR_FILE --engine awt
}

Exit-Script 0
