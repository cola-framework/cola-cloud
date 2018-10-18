# Cola eCommerce Framework
[![Travis](https://travis-ci.org/cola-framework/cola.svg?branch=master)](https://travis-ci.org/cola-framework/cola)  ![Travis](https://img.shields.io/badge/language-java-orange.svg?sytle=flag) ![Travis](https://img.shields.io/conda/pn/conda-forge/py.svg)  ![Travis](https://img.shields.io/badge/release-v0.0.1-green.svg?sytle=flag)  ![Travis](https://img.shields.io/packagist/l/doctrine/orm.svg)
### 基于SpringCloud搭建的微服务框架

# 目录结构
```
- cola
    - api
        - userclient
    - bin
    - lib
        - apidoc
        - beans
        - cache
        - cronjob
        - file
        - integration
        - jpa
        - logging
        - medias
        - ruleengine
        -search
    - log
    -platform
        - configuration
        - discovery
        - monitor
    -service
        - auth
        - oms
        - order
        - product
        - promotion
        - user
        - website
    - web
```

# 技术栈
- 代码版本控制工具 -- Git<br/>
- 项目构建工具 -- Maven<br/>
- 微服务分布式框架 -- Spring Cloud<br/>
- ORM框架 -- JPA(hibernate)<br/>
- L2 Cache -- redis/ignite<br/>
- 任务调度框架 -- Quartz<br/>
- JavaDoc生成工具 -- Swagger2<br/>
- Logging管理框架 -- Logback/ELK<br/>
- 规则引擎框架 -- drools<br/>
- 搜索引擎框架 -- Solr/Elasticsearch<br/>
- 后台管理前端框架 -- AdminLTE<br/>
- 数据库 -- MySQL<br/>



# 服务编译/构建
本项目为Maven项目，所有第三方jar包都是通过Maven管理，如果需要本地打包安装请在根目录执行如下命令：<br/>
```
\cola>mvn clean install -Dmaven.test.skip=true
```
# 应用服务启动/关闭
服务启动可以运行bin目录下的cola脚本，查看命令详情请在bin目录运行如下命令：
```
\cola\bin>cola -help
```
或
```
\cola\bin>cola --help
```
或
```
\cola\bin>cola /?
```
命令格式：
```
cola app-name [start ^| stop ^| develop ^| debug] [debug remote port] [-Djvm.args="..."] [-Drun.args="..."]
```
服务启动：
```
\cola\bin>cola registry start
```
服务停止：
```
\cola\bin>cola registry stop
```
建议开发人员使用develop或者debug模式启动服务
```
\cola\bin>cola user develop
```
或
```
\cola\bin>cola user debug
```