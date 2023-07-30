@echo off

@rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit 1

@rem Delete log folder outputs
rm -r logs/ || echo "No log files to delete"
rm hs_err_pid*.log || echo "No crash reports to delete"
echo Cleared all log files