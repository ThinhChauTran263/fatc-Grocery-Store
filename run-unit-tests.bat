@echo off
echo ========================================
echo Running Unit Tests with JUnit 4
echo ========================================
echo.

REM Clean and compile
echo [1/3] Cleaning and compiling project...
call mvn clean compile

REM Run tests
echo.
echo [2/3] Running unit tests...
call mvn test -Dtest=*ServiceTest

REM Generate JaCoCo coverage report
echo.
echo [3/3] Generating code coverage report...
call mvn jacoco:report

echo.
echo ========================================
echo Test execution completed!
echo ========================================
echo.
echo Test results: target/surefire-reports/
echo Coverage report: target/site/jacoco/index.html
echo.

pause
