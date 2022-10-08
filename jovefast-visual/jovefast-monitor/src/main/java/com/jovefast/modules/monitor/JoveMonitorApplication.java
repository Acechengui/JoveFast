package com.jovefast.modules.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 监控中心
 * 
 * @author Acechengui
 */
@EnableAdminServer
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class},scanBasePackages = {"com.jovefast"})
public class JoveMonitorApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveMonitorApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  监控中心启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
