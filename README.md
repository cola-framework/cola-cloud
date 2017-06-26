# Cola
基于SpringCloud搭建的微服务框架


## 目录说明
/bin        用于存放项目编译脚本<br/>
/lib        用于存放项目底层服务<br/>
/log        用于存放日志记录<br/>
/platform   用于存放项目平台服务<br/>
/service    用于存放项目应用服务<br/>
/web        用于存放项目前端服务<br/>


## 技术栈
代码版本控制工具 -- Git<br/>
项目构建工具 -- Maven<br/>
微服务分布式框架 -- Spring Cloud<br/>
ORM框架 -- JPA(hibernate)<br/>
L2 Cache -- redis/ignite<br/>
任务调度框架 -- Quartz<br/>
JavaDoc生成工具 -- Swagger2<br/>
Logging管理框架 -- Logback/ELK<br/>
规则引擎框架 -- drools<br/>
搜索引擎框架 -- Solr/Elasticsearch<br/>
数据库 -- MySQL<br/>

## 服务编译/构建
本项目为Maven项目，所有第三方jar包都是通过Maven管理，如果需要本地打包安装请在根目录执行如下命令：<br/>
mvn clean install -Dmaven.test.skip=true


## 应用服务启动/关闭
服务启动可以运行bin目录下的cola脚本，查看命令详情请在bin目录运行如下命令：
cola -help

服务启动命令如下：
cola discovery 0.0.1 start

服务停止命令如下：
cola discovery 0.0.1 stop

