package com.jovefast.report;

import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * 报表模块
 * 
 * @author Acechengui
 */
@EnableCustomConfig
@EnableFastFeignClients
@SpringBootApplication(exclude = {MongoAutoConfiguration.class},scanBasePackages = {"org.jeecg.modules.jmreport","com.jovefast.report"})
public class JoveReportApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveReportApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  报表模块启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
