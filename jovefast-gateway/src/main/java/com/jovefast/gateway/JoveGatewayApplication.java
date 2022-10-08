package com.jovefast.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author Acechengui
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class JoveGatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  JoveFast网关启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
