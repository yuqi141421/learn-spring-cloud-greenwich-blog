### Eureka Server集群
1）Eureka Server应用增加配置文件`application-s1.properties`，`application-s2.properties`，`application-s3.properties`。

#### application-s1.properties

```
server.port=9001

eureka.instance.hostname=peer1
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true

##
# 多个注册中心部署在一台服务器上，需使用不同的hostname并配置hosts文件。
# 在不同机器上，可直接使用ip。
# 127.0.0.1 peer1
# 127.0.0.1 peer2
# 127.0.0.1 peer3
##

# 1) hosts
eureka.client.service-url.defaultZone=http://peer1:9001/eureka/,http://peer2:9002/eureka/,http://peer3:9003/eureka/

# 2) ip
#eureka.instance.prefer-ip-address=true
#eureka.client.service-url.defaultZone=http://wxuser:123456@172.20.10.3:9002/eureka
```
#### application-s2.properties

```
server.port=9002
eureka.instance.hostname=peer2
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://peer1:9001/eureka/,http://peer2:9002/eureka/,http://peer3:9003/eureka/
```

#### application-s3.properties

```
server.port=9003
eureka.instance.hostname=peer3
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://peer1:9001/eureka/,http://peer2:9002/eureka/,http://peer3:9003/eureka/
```

2）修改Eureka Server应用启动配置。

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step1.png?v)

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step2.png)

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step3.png)

3）分别启动三个应用，访问地址

```
http://localhost:9001/
http://localhost:9002/
http://localhost:9003/
```

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step4.png)
![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step5.png)

当其余两个注册中心出现在目标注册中心的available-replicas列表中，表明它们已成功组建为一个集群。

### Eureka client集群

1）Eureka Client应用增加配置文件`application-s1.properties`，`application-s2.properties`，`application-s3.properties`

#### application-s1.properties

```
# 应用端口号
server.port=9011
# 默认注册中心区域，集群时使用英文逗号隔开
eureka.client.service-url.defaultZone=http://peer1:9001/eureka/,http://peer2:9002/eureka/,http://peer3:9003/eureka/
```

#### application-s2.properties

```
# 应用端口号
server.port=9012
# 默认注册中心区域，集群时使用英文逗号隔开
eureka.client.service-url.defaultZone=http://localhost:9001/eureka/,http://localhost:9002/eureka/,http://localhost:9003/eureka/
```

#### application-s3.properties

```
# 应用端口号
server.port=9013
# 默认注册中心区域，集群时使用英文逗号隔开
eureka.client.service-url.defaultZone=http://localhost:9001/eureka/,http://localhost:9002/eureka/,http://localhost:9003/eureka/
```

2）修改Eureka Client应用启动配置。

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step6.png)

3）分别启动三个应用，访问地址

```
http://localhost:9011/
http://localhost:9012/
http://localhost:9013/
```

4）刷新Eureka Server控制面板，可以看到三个Eureka Client已成功注册到Server中。

![image](https://gitee.com/yuqihaha/learn-spring-cloud-greenwich-blog/raw/master/img/1-3-step7.png)