/*
 Navicat Premium Data Transfer

 Source Server         : localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : 127.0.0.1:3306
 Source Schema         : jove-config

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 01/09/2023 14:07:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 233 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (233, 'sentinel-jove-gateway', 'DEFAULT_GROUP', '[\n    {\n        \"resource\": \"jove-auth-dev\",\n        \"count\": 500,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"jove-system-dev\",\n        \"count\": 1000,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"jove-report-dev\",\n        \"count\": 1000,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"jove-gen-dev\",\n        \"count\": 200,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"jove-job-dev\",\n        \"count\": 300,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }\n]', '465a049f34754e1081bae2b1a56d986a', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'json', NULL);
INSERT INTO `config_info` VALUES (234, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:\n  messages:\n    baseFolder: i18n/\n    basename: ${spring.application.name}-message\n    encoding: UTF-8\n    cacheMillis: 10000\n  main:\n    allow-circular-references: true\n    allow-bean-definition-overriding: true\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  cloud:\n    sentinel:\n      filter:\n        # sentinel 在 springboot 2.6.x 不兼容问题的处理\n        enabled: false\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 300000\n        readTimeout: 300000\n  compression:\n    request:\n      enabled: false\n      #配置请求参数压缩阈值\n      min-request-size: 8192\n    response:\n      enabled: true   \n# 企业微信\nqywx:\n  corpid: xxxxxxxxx\n  agentid: 11111111\n  corpsecret: xxxxxxxxxxxxxxxxxxxxxxxxxxxx\n  # 使用 | 分割指定多个接收人\n  target: xxxxx|aaaaaa\n\n# redisson\n# redisson:\n#   address: redis://xxx.xx.xx.xx:8379\n#   database: 9\n#   password: joveadmin\n#   #连接间隔心跳\n#   pingConnectionInterval: 1000\n\n#Springbootadmin每隔一段时间就会检查消费者的健康接口，如果返回结果超时，就会掉线\nmanagement: \n  health: \n    mail:\n      enabled: false', 'c83c41103ff5dc15c1613ac415cc73a9', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (235, 'jove-gateway-dev.yml', 'DEFAULT_GROUP', 'ribbon:\n  #连接超时时间(ms) \n  ConnectTimeout: 300000 \n  #通信超时时间(ms)\n  ReadTimeout: 300000 \n\nhystrix:\n  command:\n    default:\n      execution:\n        isolation:\n          thread:\n            #熔断超时时长ms\n            timeoutInMillisecond: 300000\n\nspring:\n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: jove-auth-dev\n          uri: lb://jove-auth-dev\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: jove-gen-dev\n          uri: lb://jove-gen-dev\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: jove-job-dev\n          uri: lb://jove-job-dev\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: jove-system-dev\n          uri: lb://jove-system-dev\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 报表模块\n        - id: jove-report-dev\n          uri: lb://jove-report-dev\n          predicates:\n            - Path=/jove-report/**\n          filters:\n            - StripPrefix=1                  \n        # 文件服务\n        - id: jove-file-dev\n          uri: lb://jove-file-dev\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        # 工作流中心服务\n        - id: jove-flowable-dev\n          uri: lb://jove-flowable-dev\n          predicates:\n            - Path=/flowable/** \n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n      - /jove-report/jmreport/**\n      - /flowable/definition/save\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /jove-report/**\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-gateway.log', 'fa657f7bab936d7f2e3111ced4d2bcbd', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (236, 'jove-auth-dev.yml', 'DEFAULT_GROUP', 'spring: \n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-auth.log\n', '122736d639b41ce8875922ced7213160', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (237, 'jove-monitor-dev.yml', 'DEFAULT_GROUP', 'spring:\n  security:\n    user:\n      name: admin\n      password: 123456\n  # mail:\n  #   host: smtp.qq.com\n  #   username: xxxxxx@foxmail.com\n  #   password: xxxxxx\n  #   default-encoding: UTF-8\n  boot:\n    admin:\n      monitor:\n        # 健康检查的超时时间 60s\n        default-timeout: 60000\n        status-interval: 15000\n        status-lifetime: 15000\n      ui:\n        title: xxx服务状态监控\n      client:\n        instance:\n          prefer-ip: true\n      notify:\n        mail:\n          enabled: false\n          to: xxxxxxx@zfdl.wecom.work\n          from: xxxxxx@foxmail.com\n', 'eee61316ef12fb97e78ce3058e93faaa', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (238, 'jove-system-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 300000\n        timeBetweenEvictionRunsMillis: 300000\n        minEvictableIdleTimeMillis: 300000\n        maxEvictableIdleTimeMillis: 600000\n        idleConnectionTestPeriod: 120\n        testConnectionOnCheckout: false\n        testConnectionOnCheckin: true\n        validationQuery: SELECT 1\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        usePingMethod: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/jove-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: +q6oUuLH<sj2\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      jove-system-prod-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: xxx.xx.xx.xx:6001\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: xx.xx.x.xxx:6001\n      namespace:\n\n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 900\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-system.log', 'c7ac52fa28cb77dfaa158abcef80fd1d', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (239, 'jove-gen-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: 127.0.0.1\n    port: 6379\n  datasource: \n    url: jdbc:mysql://127.0.0.1:3306/jove-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: +q6oUuLH<sj2\n\n            \n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.jovefast.gen.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 代码生成接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 代码生成\ngen: \n  # 作者\n  author: jovefast\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.jovefast.gen\n  # 自动去除表前缀，默认是false\n  autoRemovePre: false\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: sys_\n', 'cd32e1b7318bc9f6c9356da25262a495', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (240, 'jove-job-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: 127.0.0.1\n    port: 6379\n  datasource:\n    dynamic:\n      druid:\n        #连接池初始化大小\n        initial-size: 50 \n        #最大连接数\n        max-active: 400 \n        #最小空闲连接数\n        min-idle: 20 \n        max-wait: 1800000\n        # 申请连接时时候检测\n        test-while-idle: true\n        # 销毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        max-evictable-idle-time-millis: 1800000\n        # 毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        min-evictable-idle-time-millis: 1800000\n        # 检测是否是有效sql mysql 是 x oracle 是 select 1 from dual\n        validation-query: select 1\n        # 申请连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-borrow: false\n        # 归还连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-return: false\n        # 当数据库抛出不可恢复的异常时,抛弃该连接\n        exception-sorter: true\n        # 置访问druid监控页的账号和密码,默认没有\n        stat-view-servlet:\n          enabled: true\n          url-pattern: /*\n          exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" #不统计这些请求数据\n          stat-view-servlet: #访问监控网页的登录用户名和密码\n            url-pattern: /druid/*\n            reset-enable: false\n            login-username: admin\n            login-password: 123456\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/jove-cloud?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=GMT%2b8\n            username: root\n            password: +q6oUuLH<sj2\n          # 从库数据源\n          # slave:\n          #   driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver\n          #   url: jdbc:sqlserver://xx.xx.xx.xx:1433;databasename=DEMO\n          #   username: JoveCloud\n          #   password: xxxxxxxx\n\n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 1800\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 定时任务接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-job.log\n', '0786da0105c1fc2e096463c5c1a24685', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (241, 'jove-file-dev.yml', 'DEFAULT_GROUP', '# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/Jove-Cloud/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://192.168.1.168:9000\n  accessKey: admin\n  secretKey: joveadmin\n  bucketName: test\n\n  # spring配置\nspring: \n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n\n# swagger配置\nswagger:\n  title: 文件模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-file.log', 'a2e4da19abcbdd468d24a712abf0c1d0', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (242, 'jove-report-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n    \n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 900\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.report\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n#Minidao配置\nminidao :\n  base-package: org.jeecg.modules.jmreport.*\n\njeecg :\n  minidao-datasource:\n    jdbc-url: jdbc:mysql://127.0.0.1:3306/jove-cloud?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=GMT%2b8\n    username: root\n    password: +q6oUuLH<sj2\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      # 连接池最大连接数\n      maximum-pool-size: 400\n      # 空闲时保持最小连接数\n      minimum-idle: 20\n      # 空闲连接存活时间\n      idle-timeout: 30000\n      # 连接超时时间\n      connection-timeout: 1800000\n      #池中连接最长生命周期\n      max-lifetime: 1800000\n  jmreport:\n    #数据字典是否进行saas数据隔离(限制只能看自己的字典)\n    saas: false\n    #是否 禁用导出PDF和图片的按钮 默认为false\n    exportDisabled: false\n    #是否自动保存\n    autoSave: false\n    #自动保存间隔时间毫秒\n    interval: 20000\n    # 列索引\n    col: 300\n    #自定义项目前缀\n    customPrePath: /dev-api/jove-report\n    # 自定义API接口的前缀 #{api_base_path}的值\n    # apiBasePath: http://xx.xx.xx:81/\n    #预览分页自定义\n    pageSize:\n      - 10\n      - 20\n      - 50\n      - 100\n    #打印纸张自定义\n    printPaper:\n      - title: 标签打印\n        size:\n          - 140\n          - 100        \n    #接口超时设置（毫秒）\n    connect-timeout: 1800000\n    #Excel导出模式(fast/快、primary/精致模式，默认fast)\n    export-excel-pattern: fast\n    #Excel导出数据每个sheet的行数,每个sheet最大1048576行\n    page-size-number: 1048576\n    #excel样式超过多少行显示默认样式（只在fast模式下有效）\n    excel-style-row: 1048576\n    #预览页面的工具条 是否显示 默认true\n    viewToolbar: true\n    #设计页面表格的线是否显示 默认true\n    line: true\n    #sql数据源不写字典下拉框显示条数 版本1.4.2之后被放弃\n    select-show-total: 10\n\n  minio:\n    minio_url: http://xx.xx.1.168:9000\n    minio_name: admin\n    minio_pass: joveadmin\n    bucketName: test\n    save_path: ERP/SpotCheck/\n    template_path: templateDownload/\n\n#输出sql日志\nlogging:\n  level:\n    org.jeecg.modules.jmreport : info\n\n# swagger配置\nswagger:\n  title: 报表模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-report.log', 'ca0479e7c622c70b4ec6a24ad79c96fb', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (243, 'jove-flowable-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: 127.0.0.1\n    port: 6379\n  datasource:\n    dynamic:\n      druid:\n        #连接池初始化大小\n        initial-size: 5 \n        #最大连接数\n        max-active: 20 \n        #最小空闲连接数\n        min-idle: 10 \n        max-wait: 60000\n        # 申请连接时时候检测\n        test-while-idle: true\n        # 销毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        max-evictable-idle-time-millis: 60000\n        # 毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        min-evictable-idle-time-millis: 40000\n        # 检测是否是有效sql mysql 是 x oracle 是 select 1 from dual\n        validation-query: select 1\n        # 申请连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-borrow: false\n        # 归还连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-return: false\n        # 当数据库抛出不可恢复的异常时,抛弃该连接\n        exception-sorter: true\n        # 置访问druid监控页的账号和密码,默认没有\n        stat-view-servlet:\n          enabled: true\n          url-pattern: /*\n          exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" #不统计这些请求数据\n          stat-view-servlet: #访问监控网页的登录用户名和密码\n            url-pattern: /druid/*\n            reset-enable: false\n            login-username: admin\n            login-password: 123456\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/flowable-test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: +q6oUuLH<sj2\n      seata: false\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      jove-flowable-dev-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 127.0.0.1:8848\n      group: SEATA_GROUP\n      namespace: \n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 127.0.0.1:8848\n      namespace: \n        \n\n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 900\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.flowable\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\nlogging:\n  level:\n    com.jovefast.flowable.mapper: debug\n\n# swagger配置\nswagger:\n  title: 工作流中心模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com', '36e1064f2852ce20b9e0831c15c64ca8', '2023-09-01 06:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', '', '', '', NULL, NULL, 'yaml', NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 331 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 331, 'sentinel-jove-gateway', 'DEFAULT_GROUP', '', '[\n    {\n        \"resource\": \"jove-auth-dev\",\n        \"count\": 500,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"jove-system-dev\",\n        \"count\": 1000,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n    {\n        \"resource\": \"jove-report-dev\",\n        \"count\": 1000,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"jove-gen-dev\",\n        \"count\": 200,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"jove-job-dev\",\n        \"count\": 300,\n        \"grade\": 0,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }\n]', '465a049f34754e1081bae2b1a56d986a', '2023-09-01 14:01:36', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 332, 'application-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  messages:\n    baseFolder: i18n/\n    basename: ${spring.application.name}-message\n    encoding: UTF-8\n    cacheMillis: 10000\n  main:\n    allow-circular-references: true\n    allow-bean-definition-overriding: true\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  cloud:\n    sentinel:\n      filter:\n        # sentinel 在 springboot 2.6.x 不兼容问题的处理\n        enabled: false\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 300000\n        readTimeout: 300000\n  compression:\n    request:\n      enabled: false\n      #配置请求参数压缩阈值\n      min-request-size: 8192\n    response:\n      enabled: true   \n# 企业微信\nqywx:\n  corpid: xxxxxxxxx\n  agentid: 11111111\n  corpsecret: xxxxxxxxxxxxxxxxxxxxxxxxxxxx\n  # 使用 | 分割指定多个接收人\n  target: xxxxx|aaaaaa\n\n# redisson\n# redisson:\n#   address: redis://xxx.xx.xx.xx:8379\n#   database: 9\n#   password: joveadmin\n#   #连接间隔心跳\n#   pingConnectionInterval: 1000\n\n#Springbootadmin每隔一段时间就会检查消费者的健康接口，如果返回结果超时，就会掉线\nmanagement: \n  health: \n    mail:\n      enabled: false', 'c83c41103ff5dc15c1613ac415cc73a9', '2023-09-01 14:01:36', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 333, 'jove-gateway-dev.yml', 'DEFAULT_GROUP', '', 'ribbon:\n  #连接超时时间(ms) \n  ConnectTimeout: 300000 \n  #通信超时时间(ms)\n  ReadTimeout: 300000 \n\nhystrix:\n  command:\n    default:\n      execution:\n        isolation:\n          thread:\n            #熔断超时时长ms\n            timeoutInMillisecond: 300000\n\nspring:\n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: jove-auth-dev\n          uri: lb://jove-auth-dev\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: jove-gen-dev\n          uri: lb://jove-gen-dev\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: jove-job-dev\n          uri: lb://jove-job-dev\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: jove-system-dev\n          uri: lb://jove-system-dev\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 报表模块\n        - id: jove-report-dev\n          uri: lb://jove-report-dev\n          predicates:\n            - Path=/jove-report/**\n          filters:\n            - StripPrefix=1                  \n        # 文件服务\n        - id: jove-file-dev\n          uri: lb://jove-file-dev\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        # 工作流中心服务\n        - id: jove-flowable-dev\n          uri: lb://jove-flowable-dev\n          predicates:\n            - Path=/flowable/** \n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n      - /jove-report/jmreport/**\n      - /flowable/definition/save\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /jove-report/**\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-gateway.log', 'fa657f7bab936d7f2e3111ced4d2bcbd', '2023-09-01 14:01:36', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 334, 'jove-auth-dev.yml', 'DEFAULT_GROUP', '', 'spring: \n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-auth.log\n', '122736d639b41ce8875922ced7213160', '2023-09-01 14:01:36', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 335, 'jove-monitor-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  security:\n    user:\n      name: admin\n      password: 123456\n  # mail:\n  #   host: smtp.qq.com\n  #   username: xxxxxx@foxmail.com\n  #   password: xxxxxx\n  #   default-encoding: UTF-8\n  boot:\n    admin:\n      monitor:\n        # 健康检查的超时时间 60s\n        default-timeout: 60000\n        status-interval: 15000\n        status-lifetime: 15000\n      ui:\n        title: xxx服务状态监控\n      client:\n        instance:\n          prefer-ip: true\n      notify:\n        mail:\n          enabled: false\n          to: xxxxxxx@zfdl.wecom.work\n          from: xxxxxx@foxmail.com\n', 'eee61316ef12fb97e78ce3058e93faaa', '2023-09-01 14:01:36', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 336, 'jove-system-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 300000\n        timeBetweenEvictionRunsMillis: 300000\n        minEvictableIdleTimeMillis: 300000\n        maxEvictableIdleTimeMillis: 600000\n        idleConnectionTestPeriod: 120\n        testConnectionOnCheckout: false\n        testConnectionOnCheckin: true\n        validationQuery: SELECT 1\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        usePingMethod: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/jove-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: +q6oUuLH<sj2\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      jove-system-prod-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: xxx.xx.xx.xx:6001\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: xx.xx.x.xxx:6001\n      namespace:\n\n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 900\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-system.log', 'c7ac52fa28cb77dfaa158abcef80fd1d', '2023-09-01 14:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 337, 'jove-gen-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 127.0.0.1\n    port: 6379\n  datasource: \n    url: jdbc:mysql://127.0.0.1:3306/jove-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: +q6oUuLH<sj2\n\n            \n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.jovefast.gen.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 代码生成接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 代码生成\ngen: \n  # 作者\n  author: jovefast\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.jovefast.gen\n  # 自动去除表前缀，默认是false\n  autoRemovePre: false\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: sys_\n', 'cd32e1b7318bc9f6c9356da25262a495', '2023-09-01 14:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 338, 'jove-job-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 127.0.0.1\n    port: 6379\n  datasource:\n    dynamic:\n      druid:\n        #连接池初始化大小\n        initial-size: 50 \n        #最大连接数\n        max-active: 400 \n        #最小空闲连接数\n        min-idle: 20 \n        max-wait: 1800000\n        # 申请连接时时候检测\n        test-while-idle: true\n        # 销毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        max-evictable-idle-time-millis: 1800000\n        # 毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        min-evictable-idle-time-millis: 1800000\n        # 检测是否是有效sql mysql 是 x oracle 是 select 1 from dual\n        validation-query: select 1\n        # 申请连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-borrow: false\n        # 归还连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-return: false\n        # 当数据库抛出不可恢复的异常时,抛弃该连接\n        exception-sorter: true\n        # 置访问druid监控页的账号和密码,默认没有\n        stat-view-servlet:\n          enabled: true\n          url-pattern: /*\n          exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" #不统计这些请求数据\n          stat-view-servlet: #访问监控网页的登录用户名和密码\n            url-pattern: /druid/*\n            reset-enable: false\n            login-username: admin\n            login-password: 123456\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/jove-cloud?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=GMT%2b8\n            username: root\n            password: +q6oUuLH<sj2\n          # 从库数据源\n          # slave:\n          #   driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver\n          #   url: jdbc:sqlserver://xx.xx.xx.xx:1433;databasename=DEMO\n          #   username: JoveCloud\n          #   password: xxxxxxxx\n\n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 1800\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 定时任务接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-job.log\n', '0786da0105c1fc2e096463c5c1a24685', '2023-09-01 14:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 339, 'jove-file-dev.yml', 'DEFAULT_GROUP', '', '# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/Jove-Cloud/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://192.168.1.168:9000\n  accessKey: admin\n  secretKey: joveadmin\n  bucketName: test\n\n  # spring配置\nspring: \n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n\n# swagger配置\nswagger:\n  title: 文件模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-file.log', 'a2e4da19abcbdd468d24a712abf0c1d0', '2023-09-01 14:01:37', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 340, 'jove-report-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    lettuce:\n      shutdown-timeout: 100\n    host: 127.0.0.1\n    port: 6379\n    \n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 900\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.report\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n#Minidao配置\nminidao :\n  base-package: org.jeecg.modules.jmreport.*\n\njeecg :\n  minidao-datasource:\n    jdbc-url: jdbc:mysql://127.0.0.1:3306/jove-cloud?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=GMT%2b8\n    username: root\n    password: +q6oUuLH<sj2\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      # 连接池最大连接数\n      maximum-pool-size: 400\n      # 空闲时保持最小连接数\n      minimum-idle: 20\n      # 空闲连接存活时间\n      idle-timeout: 30000\n      # 连接超时时间\n      connection-timeout: 1800000\n      #池中连接最长生命周期\n      max-lifetime: 1800000\n  jmreport:\n    #数据字典是否进行saas数据隔离(限制只能看自己的字典)\n    saas: false\n    #是否 禁用导出PDF和图片的按钮 默认为false\n    exportDisabled: false\n    #是否自动保存\n    autoSave: false\n    #自动保存间隔时间毫秒\n    interval: 20000\n    # 列索引\n    col: 300\n    #自定义项目前缀\n    customPrePath: /dev-api/jove-report\n    # 自定义API接口的前缀 #{api_base_path}的值\n    # apiBasePath: http://xx.xx.xx:81/\n    #预览分页自定义\n    pageSize:\n      - 10\n      - 20\n      - 50\n      - 100\n    #打印纸张自定义\n    printPaper:\n      - title: 标签打印\n        size:\n          - 140\n          - 100        \n    #接口超时设置（毫秒）\n    connect-timeout: 1800000\n    #Excel导出模式(fast/快、primary/精致模式，默认fast)\n    export-excel-pattern: fast\n    #Excel导出数据每个sheet的行数,每个sheet最大1048576行\n    page-size-number: 1048576\n    #excel样式超过多少行显示默认样式（只在fast模式下有效）\n    excel-style-row: 1048576\n    #预览页面的工具条 是否显示 默认true\n    viewToolbar: true\n    #设计页面表格的线是否显示 默认true\n    line: true\n    #sql数据源不写字典下拉框显示条数 版本1.4.2之后被放弃\n    select-show-total: 10\n\n  minio:\n    minio_url: http://xx.xx.1.168:9000\n    minio_name: admin\n    minio_pass: joveadmin\n    bucketName: test\n    save_path: ERP/SpotCheck/\n    template_path: templateDownload/\n\n#输出sql日志\nlogging:\n  level:\n    org.jeecg.modules.jmreport : info\n\n# swagger配置\nswagger:\n  title: 报表模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS\n    logfile:\n      #可在线查看日志\n      enabled: true\n      external-file: logs/jove-report.log', 'ca0479e7c622c70b4ec6a24ad79c96fb', '2023-09-01 14:01:38', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 341, 'jove-flowable-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 127.0.0.1\n    port: 6379\n  datasource:\n    dynamic:\n      druid:\n        #连接池初始化大小\n        initial-size: 5 \n        #最大连接数\n        max-active: 20 \n        #最小空闲连接数\n        min-idle: 10 \n        max-wait: 60000\n        # 申请连接时时候检测\n        test-while-idle: true\n        # 销毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        max-evictable-idle-time-millis: 60000\n        # 毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接\n        min-evictable-idle-time-millis: 40000\n        # 检测是否是有效sql mysql 是 x oracle 是 select 1 from dual\n        validation-query: select 1\n        # 申请连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-borrow: false\n        # 归还连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true\n        test-on-return: false\n        # 当数据库抛出不可恢复的异常时,抛弃该连接\n        exception-sorter: true\n        # 置访问druid监控页的账号和密码,默认没有\n        stat-view-servlet:\n          enabled: true\n          url-pattern: /*\n          exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" #不统计这些请求数据\n          stat-view-servlet: #访问监控网页的登录用户名和密码\n            url-pattern: /druid/*\n            reset-enable: false\n            login-username: admin\n            login-password: 123456\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://127.0.0.1:3306/flowable-test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: +q6oUuLH<sj2\n      seata: false\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      jove-flowable-dev-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 127.0.0.1:8848\n      group: SEATA_GROUP\n      namespace: \n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 127.0.0.1:8848\n      namespace: \n        \n\n# mybatis配置\nmybatis:\n  configuration: \n    # 超时单位为秒\n    default-statement-timeout: 900\n  # 搜索指定包别名\n  typeAliasesPackage: com.jovefast.flowable\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\nlogging:\n  level:\n    com.jovefast.flowable.mapper: debug\n\n# swagger配置\nswagger:\n  title: 工作流中心模块接口文档\n  license: Powered By xxx\n  licenseUrl: https://xxx.com', '36e1064f2852ce20b9e0831c15c64ca8', '2023-09-01 14:01:38', '2023-09-01 06:01:37', NULL, '0:0:0:0:0:0:0:1', 'I', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
