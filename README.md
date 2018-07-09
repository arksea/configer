# configer
通用配置服务

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

 - 支持多台服务集群
 
   ![image](./docs/images/readme-reg-sub.png)
 

