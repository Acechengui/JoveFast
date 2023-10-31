package com.jovefast.service;

import com.gccloud.common.permission.IApiPermissionService;
import com.jovefast.common.security.utils.SecurityUtils;
import com.jovefast.system.api.model.LoginUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 控制大屏的接口权限实现.
 *
 * @author Acechengui
 * @version 1.0
 * @since 2023-10-27
 */

@Service
public class PermissionServiceImpl implements IApiPermissionService {

    @Override
    public boolean verifyApiPermission(HttpServletRequest request, String... permission) {
        // 获取当前用户的权限
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Set<String> permissions = loginUser.getPermissions();
        if (permissions.contains("*:*:*")) {
            // 全部权限标识
            return true;
        }
        // 判断当前用户是否拥有对应的权限
        for (String s : permission) {
            if (!permissions.contains(s)) {
                return false;
            }
        }
        return true;
    }
}
