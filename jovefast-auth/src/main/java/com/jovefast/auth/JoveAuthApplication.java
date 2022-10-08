package com.jovefast.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.jovefast.common.security.annotation.EnableFastFeignClients;

/**
 * 认证授权中心
 * 
 * @author Acechengui
 */
@EnableFastFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class },scanBasePackages = {"com.jovefast"})
public class JoveAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
