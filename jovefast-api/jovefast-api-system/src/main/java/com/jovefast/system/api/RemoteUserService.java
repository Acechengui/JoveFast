package com.jovefast.system.api;

import com.jovefast.system.api.domain.SysRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.jovefast.common.core.constant.SecurityConstants;
import com.jovefast.common.core.constant.ServiceNameConstants;
import com.jovefast.common.core.domain.R;
import com.jovefast.system.api.domain.SysUser;
import com.jovefast.system.api.factory.RemoteUserFallbackFactory;
import com.jovefast.system.api.model.LoginUser;

import java.util.List;

/**
 * 用户服务
 * 
 * @author Acechengui
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE,fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * @param sysUser 用户信息
     * @return 结果
     */
    @PostMapping("/user/list")
    public R<List<SysUser>> selectUserList(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/user/getInfo/{id}")
    public R<SysUser> selectUserInFoById(@PathVariable("id")Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/role/getInfo/{id}")
    public R<SysRole> selectRoleById(@PathVariable("id")Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/role/list")
    public R<List<SysRole>> selectRoleList(@RequestBody SysRole sysRole, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/user/all")
    public R<List<SysUser>> selectSysUserAll(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping("/role/all")
    public R<List<SysRole>> selectRoleAll(@RequestBody SysRole sysRole, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
