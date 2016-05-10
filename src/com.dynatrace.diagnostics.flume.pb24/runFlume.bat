@echo off

if not defined JAVA_HOME ( 
for /f "tokens=2*" %%A in ('reg query "HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment\1.7" /v JavaHome') do set JAVA_HOME=%%B
)

if not defined JAVA_HOME ( 
for /f "tokens=2*" %%A in ('reg query "HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment\1.6" /v JavaHome') do set JAVA_HOME=%%B
)

if not defined JAVA_HOME ( 
for /f "tokens=2*" %%A in ('reg query "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\1.7" /v JavaHome') do set JAVA_HOME=%%B
)

if not defined JAVA_HOME ( 
for /f "tokens=2*" %%A in ('reg query "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\1.6" /v JavaHome') do set JAVA_HOME=%%B
)

echo Using the following JAVA_HOME: %JAVA_HOME%

set FLUME_CONF=flume-conf.properties
set FLUME_AGENT=agent1

"%JAVA_HOME%\bin\java.exe" -Xmx20m -Dlog4j.configuration=file:%FLUME_HOME%\conf\log4j.properties -cp "%FLUME_HOME%\lib\*;dtFlume.jar" org.apache.flume.node.Application -f %FLUME_CONF% -n %FLUME_AGENT%