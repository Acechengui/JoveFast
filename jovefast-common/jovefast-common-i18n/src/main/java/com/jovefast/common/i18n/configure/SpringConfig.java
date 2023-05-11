package com.jovefast.common.i18n.configure;

import com.jovefast.common.i18n.resolver.DefaultLocaleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import java.io.File;
 
/**
 * Spring配置
 * 这里简单介绍下ReloadableResourceBundleMessageSource。ReloadableResourceBundleMessageSource
 * 是AbstractResourceBasedMessageSource的两个实现类之一，
 * 它提供强大的定时刷新配置文件的功能，支持应用在不重启的情况下重载配置文件，保证应用的长期稳定运行。
 * 所以，我们通过它来实现国际化信息的动态更新
 * @author Acechengui
 **/
@Configuration
public class SpringConfig {
    private static final Logger log = LoggerFactory.getLogger(SpringConfig.class);
 
    @Bean
    public LocaleResolver localeResolver(){
        return new DefaultLocaleResolver();
    }
 
    @Primary
    @Bean(name = "messageSource")
    @DependsOn(value = "messageConfig")
    public ReloadableResourceBundleMessageSource messageSource() {
        String path = ResourceUtils.FILE_URL_PREFIX + System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder() + File.separator + messageConfig.getBasename();
        log.info("国际化配置内容:{}", messageConfig);
        log.info("国际化配置路径:{}", path);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(path);
        messageSource.setDefaultEncoding(messageConfig.getEncoding());
        messageSource.setCacheMillis(messageConfig.getCacheMillis());
        return messageSource;
    }
 
    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;
 
    @Resource
    private MessageConfig messageConfig;
 
}