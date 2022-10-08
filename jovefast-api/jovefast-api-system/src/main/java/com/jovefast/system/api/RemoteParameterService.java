package com.jovefast.system.api;

import com.jovefast.common.core.constant.ServiceNameConstants;
import com.jovefast.common.core.domain.R;
import com.jovefast.system.api.factory.RemoteParameterFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description 系统参数服务
 * @Author Acechengui
 * @Date Created in 2022/7/29
 */
@FeignClient(contextId = "remoteParameterService", value = ServiceNameConstants.SYSTEM_SERVICE,fallbackFactory = RemoteParameterFallbackFactory.class)
public interface RemoteParameterService {

    /**
     * 获取参数管理的参数
     *
     * @param configKey 参数key
     */
    @GetMapping("/config/configKey/{configKey}")
    R<Boolean> getParameterInfo(@PathVariable("configKey") String configKey);
}
