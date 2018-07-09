# configer
通用配置服务主页面

  ![image](./docs/images/readme-main.png)
  
## 特性
 - 缓存与数据流
```
    客户端 《──┬─ 客户端本地缓存《──┬── Configer服务缓存《── 数据库
              └─ 客户端本地文件《──┘
```
 - 通过Json Schema定制配置值表单

  | 表单 | Schema |
  | ---- | ---- |
  |![image](./docs/images/readme-edit-config.png) | ![image](./docs/images/readme-edit-schema.png)|

 - 支持服务集群
 
   ![image](./docs/images/readme-reg-sub.png)

## 客户端实例

 - 指定配置服务地址

```
public static void main() {

    ConfigService configService = new ConfigService("172.17.150.87:8806", "net.arksea.TestProject", "online");
    
    String value = configService.getString("app.init.configer1");
    int num = configService.getInteger("app.init.config2");
    List list = configService.getList("app.init.config3");

}
```

 - 使用服务发现
 
```
public static void main() {
    
    Client client = registerClient.subscribe("net.arksea.ConfigServer");
    ConfigService configService = new ConfigService(client, "net.arksea.TestProject", "online", 3000, client.system);
    
    String value = configService.getString("app.init.configer1");
    int num = configService.getInteger("app.init.config2");
    List list = configService.getList("app.init.config3");

}
```

## 安装

 - 拷贝config-server/publish下的部署文件到tomcat/webapp
 - 配置tomcat/conf/server.xml
 - 修改WEB-INF\classes\application.properties文件Mysql数据库配置

## 集群
  服务集群的支持请参考隔壁的服务发现项目 [aregister](https://github.com/arksea/aregister)

  修改application.properties配置文件
  
  ```groovy
  
    #是否向aregister注册服务器注册服务
    config.enableRegisterService=false
    #服务的注册名
    config.serviceRegisterName=net.arksea.ConfigServer
    
    #aregister注册服务器地址
    dsf.registerAddr1=127.0.0.1:6501
    dsf.registerAddr2=127.0.0.1:6502
    #访问注册服务时使用的名字，仅用于识别，可自行取名
    dsf.clientName=config-server
  ```
