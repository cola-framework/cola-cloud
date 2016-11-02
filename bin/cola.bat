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

if not "%3" == "start" (
    if not "%3" == "stop" (
        goto paramError
    )
)

set COLA_HOME=%~dp0/..
set APP_NAME=%1
set APP_VERSION=%2
set jarFile=*cola-%APP_NAME%-%APP_VERSION%.jar

if "%3" == "start" (goto start) else (if "%3" == "stop" goto stop)
goto end

:start
:loop
set pn=%4
set pv=%5
if not "%pn%" == "" (
    set "params=%params% %pn%=%pv%"
    shift
    shift
    goto :loop
)

echo Looking for "%jarFile:~1%" ....

for /r %COLA_HOME% %%i in (%jarFile%) do (
    set filePath=%%i
    "%JAVA_EXE%" -server -javaagent:%COLA_HOME%/lib/aspectjweaver-1.8.5.jar -Dorg.aspectj.tracing.enabled=false -Daj.weaving.verbose=false -Dorg.aspectj.weaver.showWeaveInfo=false -jar %%i%params%
    set params=
    if not "%ERRORLEVEL%"=="0" (
        goto javaError
    ) else (
        goto end
    )
)
echo ERROR: Jar file is not exist.
goto end

:stop
echo WARN: Application can not be stop in Windows OS. Please look at the task list below.
tasklist /fi "imagename eq java.exe"
goto end

:javaError
echo ERROR: Illegal java command.
echo.
echo Command Prompt: %JAVA_EXE% -server -jar %params% %filePath%
goto end

:help
echo.
echo Usage:  cola app-name app-version [start ^| stop] [jar command args...]
echo.
echo Sample:
echo         1.if you want to start cola-discovery application node.
echo           cola discovery 0.0.1-SNAPSHOT start --server.port=8080
echo           or
echo           cola discovery 0.0.1-SNAPSHOT start --server.port 8080
echo.
echo         2.if you want to stop cola-discovery application node.
echo           cola discovery 0.0.1-SNAPSHOT stop
goto end

:paramError
echo ERROR: Illegal Parameters
echo.
echo Please use '-help' for more information.

:end