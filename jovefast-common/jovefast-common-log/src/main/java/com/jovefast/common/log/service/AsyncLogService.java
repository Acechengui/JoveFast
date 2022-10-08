package com.jovefast.common.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.jovefast.common.core.constant.SecurityConstants;
import com.jovefast.system.api.RemoteLogService;
import com.jovefast.system.api.domain.SysOperLog;

/**
 * 异步调用日志服务
 * 
 * @author Acechengui
 */
@Service
public class AsyncLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog)
    {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
