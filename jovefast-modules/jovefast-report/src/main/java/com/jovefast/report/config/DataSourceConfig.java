package com.jovefast.report.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Acechengui
 * @description: 数据源配置类
 */
@Configuration
public class DataSourceConfig{
    
   /**
    *  1、bean的名称必须为minidaoDataSource，否则不生效
    *  2、jeecg.minidao-datasource对应的是yml中的jeecg下的minidao-datasource，可自定义
    */
    @Bean(name="minidaoDataSource")
    @ConfigurationProperties(prefix = "jeecg.minidao-datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

}