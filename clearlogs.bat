@echo off

@rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit 1

@rem Delete log folder outputs
rm -r logs/
rm -r mayonez-base/logs/
echo Cleared all log files