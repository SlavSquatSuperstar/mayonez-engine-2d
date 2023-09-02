@echo off

rem Script Name: run.bat
rem Purpose:     Runs the project for Windows users.
rem Usage:       .\run.bat [-h/--help] [-e/--engine gl/awt]
rem Author:      SlavSquatSuperstar

rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit /b 1

rem Set variable defaults
set ENGINE="gl"
set USE_GL=1
set JAR_FILE=""

rem Parse user inputs
rem Simulate a while loop using "goto"
:read_args
if "%1"=="-h" (
    call :show_help && exit /b 0
) else if "%1"=="--help" (
    call :show_help && exit /b 0
) else if "%1"=="-e" (
    call :set_engine %2 || exit /b 1
    shift
    shift
) else if "%1"=="--engine" (
    call :set_engine %2 || exit /b 1
    shift
    shift
) else if "%1"=="" (
    rem End of inputs
    goto :main
)else (
    echo Invalid argument "%1".
    call :show_help && exit /b 1
)
goto :read_args

:main
rem Check if Java is installed
java --version
IF %ERRORLEVEL% EQU 0 (
    echo Java is installed.
) ELSE (
    echo Java is not installed.
    exit /b 1
)

rem Get jar file name
rem Can't run .jar with wildcard on Windows
for %%i in (mayonez*.jar) do (
    set JAR_FILE=%%i
)

rem Run the compiled jar file
IF %USE_GL% NEQ 0 (
    echo Launching with OpenGL Engine.
    call :run_jar %JAR_FILE% "gl"
    exit /b %ERRORLEVEL%
) ELSE (
    echo Launching with AWT Engine.
    call :run_jar %JAR_FILE% "awt"
    exit /b %ERRORLEVEL%
)

exit /b %ERRORLEVEL%

rem Functions

:show_help
rem Show help and exit with a code
echo Usage: run [-h/--help] [-e/--engine gl/awt]
exit /b 0

:set_engine
rem Set the engine type
if "%1"=="gl" (
    set USE_GL=1
) else if "%1"=="awt" (
    set USE_GL=0
) else if "%1"=="" (
    echo Option "--engine" requires one argument.
    exit /b 1
) else (
    exit /b 1
)
exit /b 0

:run_jar
rem Launch the jar with CL args
java -jar %1 --engine %2
exit /b %ERRORLEVEL%