# Cola

基于SpringCloud搭建的微服务框架


--------------------------------------
            目录说明
--------------------------------------
/bin        用于存放项目编译脚本
/lib        用于存放项目底层服务
/log        用于存放日志记录
/platform   用于存放项目平台服务
/service    用于存放项目应用服务
/web        用于存放项目前端服务


--------------------------------------
             技术栈
--------------------------------------
代码版本控制工具 -- Git
项目构建工具 -- Maven
微服务分布式框架 -- Spring Cloud
ORM框架 -- JPA(hibernate)
L2 Cache -- redis/ignite
任务调度框架 -- Quartz
JavaDoc生成工具 -- Swagger2
Logging管理框架 -- Logback/ELK
规则引擎框架 -- drools
搜索引擎框架 -- Solr/Elasticsearch
数据库 -- MySQL


--------------------------------------
           服务编译/构建
--------------------------------------
本项目为Maven项目，所有第三方jar包都是通过Maven管理，如果需要本地打包安装请在根目录执行如下命令：
mvn clean install -Dmaven.test.skip=true


--------------------------------------
          应用服务启动/关闭
--------------------------------------
服务启动可以运行bin目录下的cola脚本，查看命令详情请在bin目录运行如下命令：
cola -help

服务启动命令如下：
cola discovery 0.0.1 start

服务停止命令如下：
cola discovery 0.0.1 stop