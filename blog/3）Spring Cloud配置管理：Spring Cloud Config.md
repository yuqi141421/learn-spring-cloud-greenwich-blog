### 简介
Spring Cloud Config为分布式系统中的外部化配置提供服务器和客户端支持。通过Spring Cloud Config Server ，使用者可以管理所有环境中的所有微服务的配置信息。

### 上手
Spring Cloud Config使用git来管理所有配置文件。

1）在[github](https://github.com/new)上创建一个仓库，取名为[config-bag](https://github.com/yuqi141421/config-bag)

将仓库同步到本地，创建`eureka-client-dev.properties`文件，填写如下内容：

```
# 自定义参数
name=Akira-dev-master
age=28
```

修改完毕后提交上述文件。

2）创建git分支

```
git branch b1
git checkout b1
```

修改`eureka-client-dev.properties`文件，填写如下内容。

```
# 自定义参数
name=Akira-dev-b1
age=38
```

修改完毕后提交上述文件。

3）将修改的文件push到github仓库

push成功后，进入github对应仓库查看master和b1分支是否存在。

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/config-bag.png)

#### Config Server
1）使用IDEA快速创建Config Server

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/2-1-step1.png)

这里勾选了Eureka Discovery，是为了将config server注册为一个服务。

2）在SpringBoot启动类`ConfigServerApplication`上增加`@EnableEurekaServer`注解。

```
package com.jk.learn.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}

```

3）修改application.properties

```
server.port=9020
spring.application.name=config-server

# 会将整个git.uri下的文件下载到本地
spring.cloud.config.server.git.uri=https://github.com/yuqi141421/config-bag.git

# 在/abc目录中搜索配置文件，此处没有二级目录，省略。
#spring.cloud.config.server.git.search-paths=/abc

#eureka.client.serviceUrl.defaultZone=http://localhost:9003/eureka/,http://localhost:9001/eureka/,http://localhost:9002/eureka/
# 存在多个注册中心并且注册中心配置高可用，服务会同步到所有注册中心
eureka.client.serviceUrl.defaultZone=http://127.0.0.1:9001/eureka/
```

4）启动Eureka Server和Config Server

访问 [http://localhost:9020/master/eureka-client-dev.properties](http://localhost:9020/master/eureka-client-dev.properties)

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/config-1-step1.png)

访问 [http://localhost:9020/b1/eureka-client-dev.properties](http://localhost:9020/b1/eureka-client-dev.properties)

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/config-1-step2.png)

根据访问路径设置的不同分支，Config Server返回了不同的值。

访问注册中心 [http://localhost:9001/](http://localhost:9001/)

可以看到Config Server已经将自己注册到了注册中心里。

#### 在服务中使用
回到之前创建的`1-2-eureka-client`应用，将应用中的name和age参数对应的值改为从Config Server中获取。

1）修改pom.xml，增加如下配置

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

这样应用成为了一个config client。

2）新建==bootstrap.properties==，加入如下配置：

```
# 应用名称
spring.application.name=eureka-client
# 应用端口号
server.port=9011
# 默认注册中心区域，集群时使用英文逗号隔开
eureka.client.service-url.defaultZone=http://localhost:9001/eureka/

# 开启配置服务发现功能
spring.cloud.config.discovery.enabled=true
# 查找名为config-server的配置服务
spring.cloud.config.discovery.service-id=config-server
# 配置文件名称
spring.cloud.config.name=eureka-client
# 配置文件使用的环境
spring.cloud.config.profile=dev
# 配置文件使用分支，可衍生出服务集群中不同节点使用不同的分支
spring.cloud.config.label=master
```

3）修改application.properties

```
# 自定义参数
name=Akira
age=18
```

config client的配置文件需要放在==bootstrap.properties==中。

由于本例中使用注册中心来获取配置服务的地址，所以还需要将服务发现相关的配置放入==bootstrap.properties==中。

4）启动EurekaClientApplication后访问`http://localhost:9011/`，结果如下：
>Akira-dev-master : 28

5）修改`spring.cloud.config.label=b1`，重启应用后查看：
>Akira-dev-b1 : 38



此篇文章只是简单的介绍了如何在分布式服务中集成Spring Cloud Config。

诸如配置自动刷新等高级功能不在教程范围内，如果需要学习，可自行搜索。

接下来，将介绍功能更加强大的配置管理工具：携程Apollo，也是本人所在公司正在使用的配置管理工具。


