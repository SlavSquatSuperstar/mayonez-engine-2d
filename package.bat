@echo off

rem Script Name: package.bat
rem Purpose:     Packages the Mayonez Engine program for release on GitHub.
rem Usage:       .\package.bat
rem Author:      SlavSquatSuperstar

rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit /b 1

rem Build the Gradle Project
echo Building Mayonez Engine...
call .\gradlew.bat jar

if %ERRORLEVEL% equ 0 (
    echo Successfully built Mayonez Engine.
) else (
    echo Error building Mayonez Engine.
    exit /b 1
)

rem Replace the compiled .jar file
del dist\mayonez*.jar
copy mayonez-demos\build\libs\mayonez*.jar dist\mayonez*.jar

rem Copy resource files
copy LICENSE.txt dist\LICENSE.txt
copy mayonez-demos\*.json dist\*.json

rem Zip the dist folder
rem (Not available for Batch >:C)

rem Show success message
echo Successfully packaged Mayonez Engine.