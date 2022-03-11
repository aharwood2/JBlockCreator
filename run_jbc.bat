REM Set the local variables
setlocal

REM <---- Set your Ant path here ---->
set ANT=C:\Temp\apache-ant-1.10.12

REM <---- Set your JDK path here ---->
set "JAVA_HOME=C:\Program Files\Java\jdk-14.0.1"

REM <---- Don't change these bits unless you know what you are doing ---->
set ANT_BIN=%ANT%\bin
set PATH=%PATH%;%ANT_BIN%

ant -v run