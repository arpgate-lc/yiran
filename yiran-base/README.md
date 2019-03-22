﻿# 微服务治理

标签（空格分隔）： base config eureka hystrix zipkin

---

## 目录

-   [1. 目录](#目录)
-   [2. 概述](#概述)
-   [2. 详细说明](#详细说明)
    - [base-config](#base-config)
    - [base-eureka](#base-eureka)
    - [base-turbine](#base-turbine)
    - [base-zipkin](#base-zipkin)

## 概述

微服务治理基础服务包含如下服务，详细内容参见服务详细说明，所有服务集群内部访问使用域名方式.
规则如下：`http://servername.namespaces.svc.cluster.local:port`
示例：`http://rabbitmq.base.svc.cluster.local:15672`

|服务名称|port:nodeport|集群规模|服务说明|
|:-|:-|:-:|:-|
|rabbitmq|5672->34672<br>15672->34673|1|默认消息服务|
|base-config|8888->34888|2|集中动态配置服务，要求所有微服务均使用配置<br>管理服务，并且区分测试环境与开发环境|
|base-eureka|8761->34761<br>8761->34762|2|注册中心，使用多service方式集群部署|
|base-turbine|8989->34989|1|服务监控面板及聚合|
|base-zipkin||1|链路跟踪及日志聚合|
|base-oauth2|8887->34887|1|鉴权服务|

## 详细说明

---
#### base-config
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8888
    -   配置文件profiles include develop and production，默认为develop使用单机模式，生产和模拟测试环境中使用production
    -   使用git为配置文件仓库
    -   使用bus+rabbitmq进行配置更新消息通知
    -   打开bus-refresh端点，使用POST /actuator/bus-refresh刷新配置（webhooks中使用实现自动配置刷新）
    -   /actuator/bus-refresh基于bus通知所有监听的服务
    -   /actuator/bus-refresh/yiran-base-turbine[:port]更新指定的微服务，要求微服务必须配置spring.cloud.bus.ack.destination-service属性，原理是微服务收到请求后判断destination是否匹配
    -   示例：`http://127.0.0.1:8888/actuator/bus-refresh/yiran-base-turbine`

---

#### base-eureka
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8761
    -   配置文件profiles include develop and s1/s2，默认为develop使用单机模式，生产和模拟测试环境中使用s1/s2
    -   使用base-config作为配置文件中心
    -   使用bus+rabbitmq进行配置更新消息通知
    -   集群方式下默认为两个节点，如果需要增加节点，需要扩展profiles；可以模仿s1/s2增加s3...s4...以此类推，注意需要进行自注册

---
#### base-turbine
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8989，管理端口8990
    -   主要用于基于hystrix的微服务调用链路跟踪，监控熔断情况，请求数，错误数等
    -   配置文件profiles include develop and production，默认为develop使用单机模式，生产和模拟测试环境中使用production
    -   修改turbine.app-config: xxx配置可以聚合指定的微服务，xxx为微服务的名称
    -   监控面板访问地址/hystrix
    -   聚合流访问地址/turbine.stream

---
#### base-zipkin


---
#### base-oauth2
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8887
    -   主要用于基于jwt+oauth2的鉴权，资源控制服务
    -   配置文件profiles include develop and production，默认为develop使用单机模式，生产和模拟测试环境中使用production
    -   此基础版本使用静态的账号、密码、角色，下一步的计划是基于redis+stream将账号、密码、角色、资源等数据存放在共享缓存，利用stream进行变更通知后重新加载
    -   监控面板访问地址/hystrix
    -   聚合流访问地址/turbine.stream