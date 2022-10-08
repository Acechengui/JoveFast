package com.jovefast.auth.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jovefast.common.core.constant.CacheConstants;
import com.jovefast.common.core.constant.Constants;
import com.jovefast.common.core.exception.ServiceException;
import com.jovefast.common.redis.service.RedisService;
import com.jovefast.common.security.utils.SecurityUtils;
import com.jovefast.system.api.domain.SysUser;

/**
 * 登录密码方法
 * 
 * @author Acechengui
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisService redisService;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password)
    {
        String username = user.getUserName();

        Integer retryCount = redisService.getCacheObject(getCacheKey(username));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        if (retryCount >= CacheConstants.PASSWORD_MAX_RETRY_COUNT)
        {
            String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", CacheConstants.PASSWORD_MAX_RETRY_COUNT, CacheConstants.PASSWORD_LOCK_TIME);
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL,errMsg);
            throw new ServiceException(errMsg);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, String.format("密码输入错误%s次", retryCount));
            redisService.setCacheObject(getCacheKey(username), retryCount, CacheConstants.PASSWORD_LOCK_TIME, TimeUnit.MINUTES);
            throw new ServiceException("用户不存在/密码错误");
        }
        else
        {
            clearLoginRecordCache(username);

        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisService.hasKey(getCacheKey(loginName)))
        {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}