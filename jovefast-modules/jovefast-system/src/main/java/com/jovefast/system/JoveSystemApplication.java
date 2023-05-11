package com.jovefast.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import com.jovefast.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author Acechengui
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableFastFeignClients
@SpringBootApplication(scanBasePackages = {"com.jovefast"})
public class JoveSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
