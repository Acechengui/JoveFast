package com.jovefast.common.security.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.jovefast.common.security.interceptor.HeaderInterceptor;

/**
 * 拦截器配置
 *
 * @author Acechengui
 */
public class WebMvcConfig implements WebMvcConfigurer
{
    /** 不需要拦截地址 */
    public static final String[] EXCLUDE_URLS = { "/login", "/logout", "/refresh"};

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_URLS)
                .order(-10);
    }

    /**
     * 自定义请求头拦截器
     */
    public HeaderInterceptor getHeaderInterceptor()
    {
        return new HeaderInterceptor();
    }
}
