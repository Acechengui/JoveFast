## 开发

```bash
# 拉取项目

# 进入项目目录
cd xxx

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
``` 

浏览器访问 http://localhost:80

登录:admin/Psitadmin

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

或进入项目bin目录执行build.bat

## 注意事项
1. 本地开发请务必使用dev环境!!!,切勿使用其他环境,造成不同开发人员数据冲突.
2. 请严格按照模块划分,包名划分,尽量解耦合
3. 请勿将node_modules目录以及package-lock.json文件上传到svn!!!
4. 升级elementui版本,要记得删掉node_modules,package-lock.json,重新安装依赖

## 工程目录

~~~
Jove-UI   
├── bin            // 脚本命令目录
├── build             // 默认[勿动]
├── node_modules       // 依赖[勿动]
├── public          // 默认[勿动]
├── src             
│      └── api                              // 接口 
│      │     └── monitor                             // 监控类接口
│      │     └── system                             // 系统类接口
│      │     └── tool                             // 系统工具类接口
│      └── assets                              // 静态资源 
│      └── components                                 // 全局组件[勿动] 
│      └── directive                                 // 指令[勿动] 
│      └── layout                                // 布局[勿动] 
│      └── plugins                                // 插件
│      └── router                                // 路由
│      └── store                                // 全局状态管理
│      └── utils                                // 工具包
│      └── views                                // 视图
│      │     └── components                                // 局部组件
│      │     └── dashboard                                // 视图主体[勿动] 
│      │     └── error                                // 错误视图页
│      │     └── jimureport                           // 积木报表视图页
│      │     └── monitor                                // 监控类视图
│      │     └── system                                // 系统视图
│      │     └── tool                                // 系统工具类视图
│      │
├      └── App                                       //入口[勿动] 
├      └── main.js                                      

~~~