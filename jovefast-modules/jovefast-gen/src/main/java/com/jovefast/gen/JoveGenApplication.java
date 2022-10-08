package com.jovefast.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import com.jovefast.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 代码生成
 * 
 * @author Acechengui
 */
@EnableCustomConfig
@EnableCustomSwagger2   
@EnableFastFeignClients
@SpringBootApplication
public class JoveGenApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveGenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  代码生成模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
