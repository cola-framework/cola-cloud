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
if "%1" == "-help" goto help
if "%1" == "--help" goto help
if "%1" == "/?" goto help

set COLA_HOME=%~dp0/..
set APP_NAME=%1
set RUN_MODE=%2
set JVM_ARGS=
set RUN_ARGS=
set DEBUG_PORT=
set JAR_FILE=*cola-%APP_NAME%-*.jar

if ""%RUN_MODE%"" == ""debug"" (
	setlocal enabledelayedexpansion
	set DEBUG_PORT=%3
	set JVM_ARGS=-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=!DEBUG_PORT!,suspend=n
	goto debug
)

:loop
set pn=%3
set pv=%4
if not "%pn%" == "" (
	if "%pn%" == "-Djvm.args" (
		set "JVM_ARGS=%pv:"=%"
	) else if "%pn%" == "-Drun.args" (
		set "RUN_ARGS=%pv:"=%"
	) else (
		goto paramError
	)
    shift
    shift
    goto :loop
)

if ""%RUN_MODE%"" == ""start"" goto start
if ""%RUN_MODE%"" == ""stop"" goto stop
if ""%RUN_MODE%"" == ""develop"" goto develop
goto paramError

:start
:debug
echo Looking for "%JAR_FILE:~1%" ....

for /R %COLA_HOME% %%i in (%JAR_FILE%) do (
	setlocal enabledelayedexpansion
	set filePath=%%i
	if not "!filePath:~-12!" == "-javadoc.jar" (
		if not "!filePath:~-12!" == "-sources.jar" (
			echo [Jar_File]:!filePath!
			"%JAVA_EXE%" %JVM_ARGS% -javaagent:%COLA_HOME%/lib/aspectjweaver-1.8.5.jar -Dorg.aspectj.tracing.enabled=false -Daj.weaving.verbose=false -Dorg.aspectj.weaver.showWeaveInfo=false -jar !filePath! %RUN_ARGS%
			if not "%ERRORLEVEL%"=="0" (goto javaError)
			pause
		)
	)
)
echo ERROR: Jar file is not exist.
goto end

:stop
echo WARN: Application can not be stop in Windows OS. Please look at the task list below.
tasklist /fi "imagename eq java.exe"
goto end

:develop
:findMaven
@rem Find Mvn.cmd
if defined M2_HOME (
    goto findMavenFromM2Home
)

set MAVEN_CMD=mvn.cmd
%MAVEN_CMD% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto runmvn

echo.
echo Error: M2_HOME is not set and no 'mvn' command could be found in your PATH.
echo.
echo Please set the M2_HOME variable in your environment to match the
echo location of the Maven installation.
goto end

:findMavenFromM2Home
set M2_HOME=%M2_HOME:"=%
set MAVEN_CMD=%M2_HOME%/bin/mvn.cmd

if exist "%MAVEN_CMD%" goto runmvn

echo.
echo Error: M2_HOME is set to an invalid directory: %M2_HOME%
echo.
echo Please set the M2_HOME variable in your environment to match the
echo location of the Maven installation.
goto end

:runmvn
echo Looking for "%APP_NAME%" Maven folder ....

set s=
for /R "%COLA_HOME%" %%s in (%APP_NAME%) do (
	if exist "%%s\pom.xml" (
		setlocal enabledelayedexpansion
		echo [Maven_Folder]:%%s
		cd %%s
		if not "%RUN_ARGS%" == "" (set s=-Drun.arguments="%RUN_ARGS: =,%")
		if not "%JVM_ARGS%" == "" (set s=!s! -Djvm.argments="%JVM_ARGS%")
		%MAVEN_CMD% spring-boot:run !s!
		if not "%ERRORLEVEL%"=="0" (goto mvnError)
		pause
	)
)
echo ERROR: "%APP_NAME%" Maven folder is not exist.
goto end

:javaError
echo.
echo ERROR: Illegal java command.
echo Command Prompt: %JAVA_EXE% %JVM_ARGS% -javaagent:%COLA_HOME%/lib/aspectjweaver-1.8.5.jar -Dorg.aspectj.tracing.enabled=false -Daj.weaving.verbose=false -Dorg.aspectj.weaver.showWeaveInfo=false -jar !filePath! %RUN_ARGS%
goto end

:mvnError
echo.
echo ERROR: Illegal mvn command.
echo Command Prompt: %MVN_CMD% spring-boot:run !s!
goto end

:help
echo.
echo Usage:  cola app-name [start ^| stop ^| develop ^| debug] [-Djvm.args="..."] [-Drun.args="..."]
echo.
echo Sample:
echo         1.if you want to start cola-discovery application node.
echo           cola discovery start
echo.
echo         2.if you want to stop cola-discovery application node.
echo           cola discovery stop
echo.
echo         3.if you want to debug cola-discovery application node.
echo           cola discovery debug 9000
echo.
echo         4.if you want to start cola-discovery application node via develop mode.
echo           cola discovery develop
echo.
echo         5.if you want to add arguments to cola-discovery application node.
echo           cola discovery start -Drun.args="--server.port=8080 --spring.application.name=test" -Djvm.args="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n"
goto end

:paramError
echo.
echo ERROR: Illegal Parameters
echo Please use '-help' for more information.

:end
endlocal