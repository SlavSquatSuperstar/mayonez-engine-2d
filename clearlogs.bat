@echo off

rem Script Name: clearlogs.bat
rem Purpose:     Clears all log files and crash reports generated by the program.
rem Usage:       .\clearlogs.bat
rem Author:      SlavSquatSuperstar

rem Navigate to the project directory
set SCRIPT_DIR=%~dp0
cd %SCRIPT_DIR% || exit /b 1

rem Delete log folder outputs and crash reports
for /d /r %%i in (logs) do if exist "%%i" rd /s /q "%%i"
for /r %%i in (hs_err_pid*.log) do if exist "%%i" del /q "%%i"
echo Cleared all log files.