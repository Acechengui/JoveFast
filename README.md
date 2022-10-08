
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">jovefast v3.6.1</h1>
<h4 align="center">基于若依Cloud的 Vue/Element UI 和 Spring Boot/Spring Cloud & Alibaba 前后端分离的分布式微服务架构</h4>
<a href='https://gitee.com/wxjstudy/jove-fast'><img src='https://gitee.com/wxjstudy/jove-fast/widgets/widget_4.svg' alt='Fork me on Gitee'></img></a>


## 平台简介
基于若依Cloud的Jove-Fast微服务平台，主要集成了 [积木报表](http://jimureport.com/)
* 账号密码admin/Psitadmin
* 采用前后端分离的模式
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel.
* 分布式锁选型redisson.

## 打包发布
请进入bin目录的命令执行打包命令

## 若依Cloud
地址：[传送门](https://gitee.com/y_project/RuoYi-Cloud)

## 注意事项
1. 为区分 开发dev|测试test|生产prod 等不同环境 com.jovefast.common.core.constant类中的serviceid需要自行同步更改(后缀)
2. 本地开发请务必使用dev环境!!!,切勿使用其他环境,造成不同开发人员数据冲突.
3. 请严格按照模块划分,包名划分,尽量解耦合  
4. 提交代码时,请注意不要提交跟项目无关的文件或目录,如:.idea || target || logs
5. 本地启动项目时,一定要注意你自己当前环境,是dev,不要切换到了prod生产环境,忘了切回去,影响线上
6. 本地启动无需启动jovefast-visual-monitor模块
7. Redisson是Redis官方推荐的Java版的Redis客户端,此处我们只用它的分布式锁功能(已集成)。

用法:
```markdown
@Autowired
private RedisLock redisLock;

// lockKey 锁实例key waitTime 最多等待时间 leaseTime 上锁后自动释放锁时间  unit 时间颗粒度
redisLock.lock(lockKey);
redisLock.tryLock(lockKey, leaseTime);
redisLock.tryLock(lockKey, leaseTime, unit);
redisLock.tryLock(lockKey, waitTime, leaseTime, unit);
redisLock.unlock(lockKey);
redisLock.unlock(lock);
```

## 系统

~~~
com.jovefast     
├── jovefast-gateway         // 网关模块
├── jovefast-auth            // 认证中心
├── jovefast-api             // 接口模块
│       └── jovefast-api-system                          // 系统接口
├── jovefast-common          // 通用模块
│       └── jovefast-common-core                         // 核心模块
│       └── jovefast-common-datascope                    // 权限范围
│       └── jovefast-common-datasource                   // 多数据源
│       └── jovefast-common-log                          // 日志记录
│       └── jovefast-common-redis                        // 缓存服务
│       └── jovefast-common-seata                        // 分布式事务
│       └── jovefast-common-security                     // 安全模块
│       └── jovefast-common-swagger                      // 系统接口
├── jovefast-modules         // 业务模块
│       └── jovefast-system                              // 系统模块 
│       └── jovefast-gen                                 // 代码生成 
│       └── jovefast-job                                 // 定时任务 
│       └── jovefast-file                                // 文件服务 
│       └── jovefast-report                                // 报表服务
├── jovefast-visual          // 图形化管理模块
│       └── jovefast-visual-monitor                      // 监控中心 
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="http://processon.com/chart_image/62ad7ff21efad41af041a1d6.png?_=1655888519650"/>
