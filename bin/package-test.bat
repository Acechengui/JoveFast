@echo off
echo.
echo [信息] 打包生成test环境Web工程，生成war/jar包文件。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -P test

pause