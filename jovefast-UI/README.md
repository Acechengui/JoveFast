## 开发

```bash
# 拉取项目

# 进入项目目录
cd xxx

# 安装依赖 建议node版本不用最新，v14.18.0 上下即可
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org


# 启动服务
npm run dev
``` 

浏览器访问 http://localhost:80

登录:admin/123456

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

或进入项目bin目录执行build.bat

## 环境要求

```bash
node: 14.x
npm: 6.x
```

## 注意事项
1. 本地开发请务必使用dev环境!!!,切勿使用其他环境,造成不同开发人员数据冲突.
2. 请严格按照模块划分,包名划分,尽量解耦合
3. 请勿将node_modules目录以及package-lock.json文件上传到svn!!!
4. 升级elementui版本,要记得删掉node_modules,package-lock.json,重新安装依赖

## Git/Svn提交规范
* feat（新功能）：用于描述新增的功能或特性。例如：添加用户注册功能、实现图片上传功能等。
* fix（修复）：用于描述修复的bug或错误。例如：修复登录页面无法显示的问题、修复数据保存错误的bug等。
* docs（文档）：用于描述文档注释的更改或新增。例如：更新API文档、添加函数注释等。
* style（代码格式）：用于描述不影响代码运行的格式调整或变动。例如：调整缩进、统一命名风格等。
* refactor（重构）：用于描述重构或优化代码的变动，既不增加新功能，也不修复bug。例如：重构登录模块、优化数据库查询逻辑等。
* perf（性能优化）：用于描述对代码进行的性能优化的变动。例如：优化算法、减少内存占用等。
* test（测试）：用于描述增加或修改测试代码的变动。例如：添加单元测试、修复测试用例等。
* chore（构建或辅助工具）：用于描述构建过程或辅助工具的变动。例如：更新构建脚本、添加代码检查工具等。
* revert（回退）：用于描述回退到之前提交的变动。例如：回退到上一个版本、撤销某次提交等。
* build（打包）：用于描述打包或构建过程的变动。例如：更新打包配置、修改构建流程等。

```bash
格式: 类型:描述  
类型只能是以上类型,描述则是自己所作内容
```


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