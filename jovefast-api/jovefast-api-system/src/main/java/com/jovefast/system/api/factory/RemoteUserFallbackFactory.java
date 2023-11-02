package com.jovefast.system.api.factory;

import com.jovefast.system.api.domain.SysRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.jovefast.common.core.domain.R;
import com.jovefast.system.api.RemoteUserService;
import com.jovefast.system.api.domain.SysUser;
import com.jovefast.system.api.model.LoginUser;

import java.util.List;

/**
 * 用户服务降级处理
 * 
 * @author Acechengui
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public R<LoginUser> getUserInfo(String username, String source)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source)
            {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> selectUserList(SysUser sysUser, String source) {
                return R.fail("获取用户列表失败:" + throwable.getMessage());
            }

            @Override
            public R<SysUser> selectUserInFoById(Long id, String source) {
                return R.fail("获取用户信息失败:" + throwable.getMessage());
            }
            @Override
            public R<SysRole> selectRoleById(Long id, String source) {
                return R.fail("根据ID获取角色信息失败:" + throwable.getMessage());
            }
            @Override
            public R<List<SysRole>> selectRoleList(SysRole sysRole, String source) {
                return R.fail("获取角色列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> selectSysUserAll(SysUser sysUser,String source) {
                return R.fail("获取用户信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysRole>> selectRoleAll(SysRole sysRole,String source) {
                return R.fail("获取全部角色信息失败:" + throwable.getMessage());
            }
        };
    }
}
