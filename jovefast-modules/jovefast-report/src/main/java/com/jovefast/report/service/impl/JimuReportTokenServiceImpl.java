package com.jovefast.report.service.impl;

import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.security.service.TokenService;
import com.jovefast.common.security.utils.SecurityUtils;
import com.jovefast.system.api.model.LoginUser;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Acechengui
 *
 * 自定义报表鉴权(如果不进行自定义，则所有请求不做权限控制)
 */
@Component
public class JimuReportTokenServiceImpl implements JmReportTokenServiceI {

    @Autowired
    private TokenService tokenService;

    /**
     * 通过请求获取Token
     */
    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getParameter("token");
        String jmToken = request.getHeader("token");
        if (token == null || token.length() == 0) {
            token = jmToken;
        }
        LoginUser loginUser = tokenService.getLoginUser(token);
        if (loginUser != null) {
            return token;
        }
        return "";
    }

    /**
     * 获取登录人用户名
     */
    @Override
    public String getUsername(String s) {
        LoginUser loginUser = tokenService.getLoginUser(s);
        return loginUser.getUsername();
    }

    /**
     * Token校验
     */
    @Override
    public Boolean verifyToken(String s) {
        if (s != null && s.length() > 0) {
            LoginUser loginUser = tokenService.getLoginUser(s);
            return loginUser !=null;
        }
        return false;
    }


    /**
     *  自定义请求头
     */
    @Override
    public HttpHeaders customApiHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("X-Access-Token", SecurityUtils.getToken());
        return header;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 将所有信息存放至map 解析sql会根据map的键值解析,可自定义其他值
        Map<String, Object> map = new HashMap<>(20);
        LoginUser loginUser = tokenService.getLoginUser(token);
        map.put("sysUserCode",loginUser.getUsername());
        //设置当前日期（年月日）
        map.put("sysData",DateUtils.getDate());
        //设置昨天日期（年月日）
        map.put("sysYesterDay",DateUtils.getyesterday());
        //设置当前登录用户昵称
        map.put("sysUserName",loginUser.getSysUser().getNickName());
        //设置当前登录用户部门ID
        map.put("deptId",loginUser.getSysUser().getDeptId());
        //设置当前登录用户部门描述
        map.put("describe",loginUser.getSysUser().getDept().getDescribes());
        return map;
    }
}
