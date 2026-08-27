@ECHO OFF
SET DIRNAME=%~dp0
SET APP_HOME=%DIRNAME%
IF "%JAVA_HOME%"=="" (
  SET JAVACMD=java.exe
) ELSE (
  SET JAVACMD=%JAVA_HOME%\bin\java.exe
)
"%JAVACMD%" -classpath "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
