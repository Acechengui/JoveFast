package com.jovefast.common.i18n.configure;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executor;
 
/**
 * Nacos配置管理器
 * 第一，应用启动时，通过init方法初始化配置国际化配置。
 *
 * 第二，在init方法中，会拉取Nacos服务端配置并写入到本地缓存，同时注册一个监听器，实时监听配置的变化并及时更新本地配缓存。
 *
 * 最后，读取的命名空间通过spring.cloud.nacos.config.dNamespace指定，如果没有取到，则取默认值。
 * @author Acechengui
 **/
@Component
public class NacosConfig {

    private static final Logger log = LoggerFactory.getLogger(NacosConfig.class);
    @Autowired
    public void init() {
        serverAddr = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.server-addr");
        dNamespace = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.dNamespace");
        if (StringUtils.isEmpty(dNamespace)) {
            dNamespace = DEFAULT_NAMESPACE;
        }
        initTip(null);
        initTip(Locale.SIMPLIFIED_CHINESE);
        initTip(Locale.US);
        initTip(new Locale("th","TH"));
        log.info("初始化系统参数成功!应用名称:{},Nacos地址:{},命名空间:{}", applicationName, serverAddr, dNamespace);
    }
 
    private void initTip(Locale locale) {
        String content = null;
        String dataId = null;
        ConfigService configService = null;
        try {
            if (locale == null) {
                dataId = messageConfig.getBasename() + ".properties";
            } else {
                dataId = messageConfig.getBasename() + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties";
            }
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            properties.put(PropertyKeyConst.NAMESPACE, dNamespace);
            configService = NacosFactory.createConfigService(properties);
            content = configService.getConfig(dataId, DEFAULT_GROUP, 5000);
            if (StringUtils.isEmpty(content)) {
                log.warn("配置内容为空,跳过初始化!dataId:{}", dataId);
                return;
            }
            log.info("初始化国际化配置!配置内容:{}", content);
            saveAsFileWriter(dataId, content);
            setListener(configService, dataId, locale);
        } catch (Exception e) {
            log.error("初始化国际化配置异常!异常信息:{}", e);
        }
    }
 
    private void setListener(ConfigService configService, String dataId, Locale locale) throws com.alibaba.nacos.api.exception.NacosException {
        configService.addListener(dataId, DEFAULT_GROUP, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("接收到新的国际化配置!配置内容:{}", configInfo);
                try {
                    initTip(locale);
                } catch (Exception e) {
                    log.error("初始化国际化配置异常!异常信息:{}", e);
                }
            }
 
            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }
 
    private void saveAsFileWriter(String fileName, String content) {
        String path = System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder();
        try {
            fileName = path + File.separator + fileName;
            File file = new File(fileName);
            FileUtils.writeStringToFile(file, content);
            log.info("国际化配置已更新!本地文件路径:{}", fileName);
        } catch (IOException e) {
            log.error("初始化国际化配置异常!本地文件路径:{}异常信息:{}", fileName, e);
        }
    }
 
    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 命名空间
     */
    @Value("${spring.cloud.nacos.config.namespace}")
    private String dNamespace;
    /**
     * 服务器地址
     */
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;
 
    @Autowired
    private MessageConfig messageConfig;
 
    @Autowired
    private ConfigurableApplicationContext applicationContext;
 
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
 
    private static final String DEFAULT_NAMESPACE = "31f629db-79df-469e-bb5c-de1593f4d08f";
 
}