@echo off


::使用说明
::1、本文件放在项目里与pom.xml、readme.txt放一起
::2、如果应用改变的话只需要修改变量jarFile即可
::应用名称，换应用后只需要修改这里即可
set jarFile=wfengine.jar

::菜单提示信息
:menu
echo   ==================================
echo   = 1::uat打包并上传测试服务器     =
echo   = 2::prd打包并上传正式服务器     =
echo   = 3::退出本窗口                  =
echo   = q::退出本窗口                  =
echo   ==================================
echo.
::判断模块
set /p input=-^> 请输入选择: 
cls
if "%input%"=="1" goto uat
if "%input%"=="2" goto prd
if "%input%"=="3" goto exit
if "%input%"=="q" goto exit

:uat
::测试环境配置信息及打包
set Ip=10.92.82.44
set user=oaftp
set password=h9x0dxl6
set uploadPath=/home/hrpamgt/simbestboot
call mvn clean package -Dmaven.test.skip=true -Puat

::跳转上传jar包模块
call :upload
goto menu


:prd
::生产环境配置信息及打包
set uploadPath=/cmcc/apps
call mvn clean package -Dmaven.test.skip=true -Pprd
::正式环境10.92.82.140用户名及密码
::测试环境配置信息及打包
set Ip=10.92.82.140
set user=oaapp
set password=9NPp#%%p$
::跳转上传jar包模块
call :upload


::正式环境10.92.82.141用户名及密码
::测试环境配置信息及打包
set Ip=10.92.82.141
set user=oaapp
set password=3%%!9Mdj9
::跳转上传jar包模块
call :upload
goto menu

::结束标签(最好放到upload标签上面)
:exit
exit

::上传j文件到相应服务器
:upload
set d="%cd%"
set ftpConf=ftp.conf
cd /D %d%
echo open %Ip%>%ftpConf%
echo %user%>>%ftpConf%
echo %password%>>%ftpConf%
echo cd %uploadPath%>>%ftpConf%
echo lcd target>>%ftpConf%
echo bin>>%ftpConf%
echo rename %jarFile% %jarFile%.%date:~0,4%%date:~5,2%%date:~8,2%-%time:~0,5%.bak>>%ftpConf%
echo put %jarFile%>>%ftpConf%
echo close>>%ftpConf%
echo bye>>%ftpConf%
::应用ftp进行相应上传操作
ftp -i -s:"%d%\%ftpConf%"
::删除临时文件
del %ftpConf%
echo =====================================================================================
echo =====================================================================================
echo ==文件已上传到 %Ip% 的目录 %uploadPath%，请确认并重启应用！
echo =====================================================================================
echo =====================================================================================
echo.
echo.
