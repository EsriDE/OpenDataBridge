@echo off

if "%OS%" == "Windows_NT" setlocal

cd ..
set OPENDATA_BRDIGE_HOME=%cd%
cd %OPENDATA_BRDIGE_HOME%

set PROXY_OPTS=-Dhttp.proxyHost=<Host> -Dhttp.proxyPort=<Port> -Dhttps.proxyHost=<Host> -Dhttps.proxyPort=<Port>

set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

set CLASSPATH=%OPENDATA_BRDIGE_HOME%\classes;%OPENDATA_BRDIGE_HOME%\lib\*;
echo using CLASSPATH %CLASSPATH%

if not "%JAVA_HOME%" == "" goto gotJavaHome
echo the JAVA_HOME environment variable is not defined
echo At least this variable is needed to run this program
goto end

:gotJavaHome
echo using JAVA_HOME %JAVA_HOME%
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\javaw.exe" goto noJavaHome
goto startProgram

:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
echo NB: JAVA_HOME should point to a JDK not a JRE
goto end
:okJavaHome


rem Set standard command for invoking Java.
rem Note that NT requires a window name argument when using start.
rem Also note the quoting as JAVA_HOME may contain spaces.

:startProgram
set _RUNJAVA="%JAVA_HOME%\bin\java"

set _EXECJAVA=%_RUNJAVA%
set MAINCLASS=esride.opendatabridge.application.AppStarter



echo using CMD_LINE_ARGS %CMD_LINE_ARGS%
%_EXECJAVA% %PROXY_OPTS% -Dopendatabridge.home=%OPENDATA_BRDIGE_HOME% -classpath "%CLASSPATH%" %MAINCLASS% %CMD_LINE_ARGS%

:end