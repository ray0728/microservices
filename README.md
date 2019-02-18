基于Spring cloud的资源共享服务集
包含以下服务：

#config
服务配置分发
同时支持本地配置文件分发与git配置文件分发（目前使用github）

#discovey
服务注册与发现
所有服务均注册在此服务上

#auth
Oauth2 验证服务
目前支持 authorization code 验证方式，并提供使用RSA加密后的JWT token

#account
账户管理服务
对外提供账户增删改接口（以JSON格式返回）
对外提供群组增删改查接口（以JSON格式返回）
*除查询账户信息接口（/account/info）允许匿名访问外，其余接口均需要通过验证登录，部分接口需管理员权限
*仅在auth服务查询账户信息时返回完整信息，其余查询调用均屏蔽关键字段（密码）
*数据存储使用mysql+mybatis
*二级缓存使用redis

#resource
资源存储查询服务
对外提供资源增删改查，以及评论增删改查接口（以JSON格式返回）
*所有接口均需通过验证登录
*数据存储使用mysql+mybatis
*二级缓存使用redis

#gateway
zuul网关服务
提供统一的操作界面（使用thymeleaf模板）
