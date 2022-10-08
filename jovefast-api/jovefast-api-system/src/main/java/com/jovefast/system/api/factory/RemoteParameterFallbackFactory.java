package com.jovefast.system.api.factory;

import com.jovefast.common.core.domain.R;
import com.jovefast.system.api.RemoteParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Description Description
 * @Author Acechengui
 * @Date Created in 2022/7/29
 */
@Component
public class RemoteParameterFallbackFactory implements FallbackFactory<RemoteParameterService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteParameterFallbackFactory.class);

    @Override
    public RemoteParameterService create(Throwable throwable) {
        log.error("系统参数服务调用失败:{}", throwable.getMessage());
        return new RemoteParameterService()
        {

            /**
             * 获取参数管理的参数
             *
             * @param configKey
             */
            @Override
            public R<Boolean> getParameterInfo(String configKey) {
                return R.fail("获取系统参数失败:" + throwable.getMessage());
            }
        };
    }
}
