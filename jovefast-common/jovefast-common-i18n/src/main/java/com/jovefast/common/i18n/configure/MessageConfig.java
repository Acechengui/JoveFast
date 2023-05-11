package com.jovefast.common.i18n.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
 
/**
 * 国际化配置
 * @author Acechengui
 **/
@RefreshScope
@Component
@ConfigurationProperties(prefix = "spring.messages")
public class MessageConfig {
 
    /**
     * 国际化文件目录
     */
    private String baseFolder;
 
    /**
     * 国际化文件名称
     */
    private String basename;
 
    /**
     * 国际化编码
     */
    private String encoding;
 
    /**
     * 缓存刷新时间
     */
    private long cacheMillis;

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public long getCacheMillis() {
        return cacheMillis;
    }

    public void setCacheMillis(long cacheMillis) {
        this.cacheMillis = cacheMillis;
    }
}