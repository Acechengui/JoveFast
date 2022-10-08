package com.jovefast.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import com.jovefast.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 定时任务
 * 
 * @author Acechengui
 */
@EnableCustomConfig
@EnableCustomSwagger2   
@EnableFastFeignClients
@SpringBootApplication(scanBasePackages = {"com.jovefast"})
public class JoveJobApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  定时任务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
