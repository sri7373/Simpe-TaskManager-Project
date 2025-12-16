@echo off
echo ========================================
echo Starting Task Manager Backend on Windows
echo ========================================
echo.

cd /d "%~dp0"

echo Cleaning and building project...
call mvn clean spring-boot:run

pause
