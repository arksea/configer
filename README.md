# configer
通用配置服务主页面

  ![image](./docs/images/readme-main.png)
  
### 特性
 - 缓存与数据流
```
    客户端 《──┬─ 客户端本地缓存《──┬── Configer服务缓存《── 数据库 《── Web修改工具
              └─ 客户端本地文件《──┘
```
 - 通过Json Schema定制配置值表单

  | 表单 | Schema |
  | ---- | ---- |
  |![image](./docs/images/readme-edit-config.png) | ![image](./docs/images/readme-edit-schema.png)|

 - 支持服务集群
 
   ![image](./docs/images/readme-reg-sub.png)

### 客户端实例

 - 指定配置服务地址

```
public static void main() {

    ConfigService configService = new ConfigService("172.17.150.87:8806", 
        "net.arksea.TestProject", "online", 3000);
    
    String value = configService.getString("app.init.configer1");
    int num = configService.getInteger("app.init.config2");
    List list = configService.getList("app.init.config3");

}
```

 - 使用服务发现
 
```
public static void main() {
    
    Client client = registerClient.subscribe("net.arksea.ConfigServer");
    ConfigService configService = new ConfigService(client, 
        "net.arksea.TestProject", "online", 3000);
    
    String value = configService.getString("app.init.configer1");
    int num = configService.getInteger("app.init.config2");
    List list = configService.getList("app.init.config3");

}
```

### 安装

 - 拷贝config-server/publish下的部署文件到tomcat/webapp
 - 配置tomcat/conf/server.xml
 - 修改WEB-INF\classes\application.properties文件Mysql数据库配置

### 集群
  服务集群的支持请参考隔壁的服务发现项目 [aregister](https://github.com/arksea/aregister)

  修改application.properties配置文件, 将 'config.enableRegisterService' 改为 true 
  
   *注意:如果是以本地测试profile=development启动服务，服务注册名将会添加'DEV'后缀*
  
  ```
  
    #是否向aregister注册服务器注册服务
    config.enableRegisterService=true
    #服务的注册名
    config.serviceRegisterName=net.arksea.ConfigServer
    
    #aregister注册服务器地址
    dsf.registerAddr1=127.0.0.1:6501
    dsf.registerAddr2=127.0.0.1:6502
    #访问注册服务时使用的名字，仅用于识别，可自行取名
    dsf.clientName=config-server
  ```

### 缓存策略

 - 应用启动后每项配置值的初次访问，优先从配置服务读取，防止本地备份文件的值与服务不同步
 - 访问配置服务如果失败，将会读取本地备份文件中的值
 - 初次访问之后的每次访问，都将优先返回缓存内容，即使缓存过期也不会等待更新
 - 访问配置项时，如果缓存过期将在直接返回缓存值的同时向配置服务发起异步请求以更新缓存内容，缓存过期时间由 'config.cache.timeout' 确定，单位为毫秒


### 其他
- Firefox下无法正常显示图标的解决办法

  Firefox在版本62之前的默认配置没有允许webcomponents，请在地址栏输入 about:config， 将以下两项改为true，重启浏览器即可

      dom.webcomponents.customelements.enabled
      dom.webcomponents.shadowdom.enabled

