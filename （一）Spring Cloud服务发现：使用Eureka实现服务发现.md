## Spring Cloud Netflix Eureka简介
Spring Cloud Netflix Eureka是Spring Cloud生态系统中的服务治理组件之一，基于Netflix Eureka二次封装。

Netflix Eureka是Netflix(网飞)公司开源的一款基于REST的服务发现组件。支持CAP原则中的AP。

## 上手
### 创建服务注册中心
1）使用IDEA快速创建。
选择工具框 File -> New -> Module -> Spring Initializr，依次填写每页信息。
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-1-step1.png)
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-1-step2.png)
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-1-step3.png)

2）在SpringBoot启动类`EurekaServerApplication`上增加`@EnableEurekaServer`注解。

```
package com.jk.learn.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}

```

3）修改application.properties

```
# 应用名称
spring.application.name=eureka-server
# 应用端口号
server.port=9001
# 当前应用的主机名称
eureka.instance.hostname=localhost
# 开启客户端健康检查
eureka.client.healthcheck.enabled=true
# 是否将自己注册到EurekaServer中。默认true，单点不需要
eureka.client.register-with-eureka=false
# 是否从EurekaServer获取注册信息，默认true，单点不需要
eureka.client.fetch-registry=false
# 默认注册中心区域，集群时使用英文逗号隔开
eureka.client.service-url.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/

```
4）启动应用，然后访问http://localhost:9001/，界面如下：
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-1-step4.png)

### 创建服务
1） 使用IDEA快速创建。
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-2-step1.png)
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-2-step2.png)

2）修改SpringBoot启动类，增加`@EnableDiscoveryClient`注解。

```
package com.jk.learn.eurekaclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;

    @GetMapping("/")
    public String helloWorld() {
        return name + " : " + age;
    }

}

```

从Spring Cloud Edgware开始，`@EnableDiscoveryClient`可省略。只要加上相关依赖，并进行相应配置，即可将微服务注册到服务发现组件上。

如需禁用服务自动注册，将`@EnableDiscoveryClient`改为`@EnableDiscoveryClient(autoRegister = false)`或者设置`spring.cloud.service-registry.auto-registration.enabled=false`。

3）修改application.properties

```
# 应用名称
spring.application.name=eureka-client
# 应用端口号
server.port=9011
# 默认注册中心区域，集群时使用英文逗号隔开
eureka.client.service-url.defaultZone=http://localhost:9001/eureka/

# 自定义参数
name=Akira
age=18
```
4）client启动后，访问http://localhost:9011/
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-2-step3.png)

5）再次刷新Eureka Server，可看到EUREKA-CLIENT已注册到Server中。
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-2-step4.png)
