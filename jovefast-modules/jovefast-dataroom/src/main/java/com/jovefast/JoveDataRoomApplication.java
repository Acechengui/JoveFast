package com.jovefast;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.gccloud.common.constant.CommonConst;
import com.gccloud.dataroom.core.constant.DataRoomConst;
import com.gccloud.dataset.constant.DatasetConstant;
import com.jovefast.common.security.annotation.EnableCustomConfig;
import com.jovefast.common.security.annotation.EnableFastFeignClients;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author hongyang
 * @version 1.0
 * @date 2023/3/13 10:55
 */
@EnableCustomConfig
@EnableFastFeignClients
@SpringBootApplication(scanBasePackages = {DataRoomConst.ScanPackage.COMPONENT, DatasetConstant.ScanPackage.COMPONENT, CommonConst.ScanPackage.COMPONENT})
@MapperScan(value = {DataRoomConst.ScanPackage.DAO, DatasetConstant.ScanPackage.DAO})
public class JoveDataRoomApplication {

    public static void main(String[] args) {

        SpringApplication.run(JoveDataRoomApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  可视化大屏模块启动成功   ლ(´ڡ`ლ)ﾞ ");
        System.out.println("STARTING SUCCESS");
    }

    /**
     * 分页插件
     *
     * @return PaginationInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
