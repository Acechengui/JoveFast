package com.jovefast.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.jovefast.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 文件服务
 * 
 * @author Acechengui
 */
@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class },scanBasePackages = {"com.jovefast"})
public class JoveFileApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JoveFileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
