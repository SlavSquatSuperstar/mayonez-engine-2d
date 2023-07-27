@echo off

@rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit 1

@rem Delete log folder outputs
rm -r logs/
rm hs_err_pid*.log
echo Cleared all log files