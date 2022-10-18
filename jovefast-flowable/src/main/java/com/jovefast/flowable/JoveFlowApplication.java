package com.jovefast.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import com.jovefast.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 工作流中心
 * 
 * @author Acechengui
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableFastFeignClients
@SpringBootApplication
public class JoveFlowApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveFlowApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  工作流中心启动成功   ლ(´ڡ`ლ)ﾞ ");
    }
}
