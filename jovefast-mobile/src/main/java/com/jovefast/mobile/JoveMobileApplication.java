package com.jovefast.mobile;

import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 移动APP模块
 *
 * @author Acechengui
 */
@EnableCustomConfig
@EnableFastFeignClients
@SpringBootApplication(scanBasePackages = {"com.jovefast"})
public class JoveMobileApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(JoveMobileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  移动APP模块启动成功   ლ(´ڡ`ლ)ﾞ");
        System.out.println("STARTING SUCCESS");
    }
}
