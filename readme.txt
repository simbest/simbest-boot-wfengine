1.编译工程 mvn compile
2.测试工程 mvn test
3.分析类库依赖 mvn dependency:tree --> tree.txt

4.本地启动工程 
4.1 后端启动方式：以IntelliJ IDEA为例
4.1.1 编辑Run/Debug Configurations，在中下部Active Profiles 输入需要连接的环境，如：dev
4.1.2 右侧Maven Projects的Profiles将需要连接的环境打勾，如：dev

4.2 前端启动方式
4.2.1 修改pom.xml 中<addResources>false</addResources>，将值改为true
4.2.2 src\main\resources目录的application.properties文件第三行#删掉，第四行增加#
4.2.3 根目录运行 mvn spring-boot:run -Dfile.encoding=utf-8 -Pdev -Dspring-boot.run.profiles=dev
注意： application.properties文件调整后，执行mvn compile后将target\classes\application.properties文件内容替换到src\main\resources\application-dev.properties

5.打包 
测试环境 mvn clean package -Dmaven.test.skip=true -Puat
生产环境 mvn clean package -Dmaven.test.skip=true -Pprd


6.运行（本地环境）
java -jar target/wfengine.jar
java -jar target/wfengine.jar --server.port=8080 (指定端口)
6.1 后台运行（测试环境）
cd /data/web/wfengine
nohup /usr/local/jdk1.8/jre/bin/java -jar wfengine.jar --server.port=8080 > /dev/null 2>&1 &
tailf boot_app_logs/wfengine/log_info.log

部署微信URL配置：http://wfenginewx.fijo.com.cn/anonymous/portal/wx63474943d6e6c61d

常见启动问题：
凡是启动的时候，没有出现大大的Simple & Best字样，都是启动有问题，例如：
Failed to load property source from location 'classpath:/application.properties'
解决办法：
通过命令行编译一下代码 mvn compile -Dfile.encoding=utf-8 -Pdev -Dspring-boot.run.profiles=dev

