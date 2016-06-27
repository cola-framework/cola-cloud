@echo off

@rem Find java.exe
if defined JAVA_HOME (
    goto findJavaFromJavaHome
)

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto continue

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto end

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto continue

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto end

:continue
if "%1" == "-help" (
    goto help
)

if not "%2" == "start" (
    if not "%2" == "stop" (
        goto paramError
    )
)

set param1=%1
set jarPrefix=cola-%param1%*.jar

echo %jarPrefix%

for /f "delims=" %%i in (*) do (
    echo %%i
)

%JAVA_EXE% -jar

goto end

:notExist
echo Application jar file is not exist.
goto end

:help
echo.
echo Usage:  cola app-name [start ^| stop] [jar command args...]
echo.
echo Sample:
echo         1.if you want to start discovery application node.
echo           cola discovery start --server.port=8080
echo.
echo         2.if you want to stop discovery application node.
echo           cola discovery stop
goto end

:paramError
echo Illegal Parameters
echo Please use '-help' for more information.

:end