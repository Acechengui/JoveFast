package com.jovefast.common.i18n.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * 国际化配置获取工具类
 * 通过注入MessageSource，通过getMessage方法获取对应的国际化配置
 * @author Acechengui
 **/
@Component
public class PropertiesTools {
    private static final Logger log = LoggerFactory.getLogger(PropertiesTools.class);
 
    public String getProperties(String name,@Nullable Object[] args) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(name, args, locale);
        } catch (NoSuchMessageException e) {
            log.error("获取配置异常!异常信息:{}", e);
        }
        return null;
    }
 
    @Autowired
    private MessageSource messageSource;
 
}