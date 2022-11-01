package com.jovefast.common.security.handler;

import javax.servlet.http.HttpServletRequest;

import com.jovefast.common.core.exception.CheckedException;
import com.jovefast.common.core.exception.auth.NotLoginException;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.jovefast.common.core.constant.HttpStatus;
import com.jovefast.common.core.exception.DemoModeException;
import com.jovefast.common.core.exception.InnerAuthException;
import com.jovefast.common.core.exception.ServiceException;
import com.jovefast.common.core.exception.auth.NotPermissionException;
import com.jovefast.common.core.exception.auth.NotRoleException;
import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.common.core.web.domain.AjaxResult;

import java.util.Objects;


/**
 * 全局异常处理器
 * 
 * @author Acechengui
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public AjaxResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request)
    {
        log.error("请求地址'{}',权限码校验失败'{}'", request.getRequestURI(), e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有访问权限，请联系管理员授权");
    }

    /**
     * 无效token异常
     */
    @ExceptionHandler(NotLoginException.class)
    public AjaxResult handleNotLoginException(NotPermissionException e, HttpServletRequest request)
    {
        log.error("请求地址'{}',token校验失败'{}'", request.getRequestURI(), e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "无效的令牌");
    }


    /**
     * sqlserver sql异常
     */
    @ExceptionHandler(SQLServerException.class)
    public AjaxResult sqlServerException(SQLServerException e, HttpServletRequest request)
    {
        log.error("sql执行失败:{}",e.getMessage());
        return AjaxResult.error(HttpStatus.ERROR, "服务内部出现错误,请联系管理员");
    }

    /**
     * feign 远程调用异常
     */
    @ExceptionHandler(RetryableException.class)
    public AjaxResult retryableException(RetryableException e, HttpServletRequest request)
    {
        log.error("feign远程调用异常:{}",e.getMessage());
        return AjaxResult.error(HttpStatus.ERROR, "服务调用超时");
    }

    /**
     * mybatis异常
     */
    @ExceptionHandler(DataAccessException.class)
    public AjaxResult accessException(DataAccessException e, HttpServletRequest request)
    {
        log.error("mybatis执行异常:{}",e.getMessage());
        return AjaxResult.error(HttpStatus.ERROR, "服务内部出现错误,请联系管理员");
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public AjaxResult handleNotRoleException(NotRoleException e, HttpServletRequest request)
    {
        log.error("请求地址'{}',角色权限校验失败'{}'", request.getRequestURI(), e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有访问权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        log.error("请求地址'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 自定义检查异常
     */
    @ExceptionHandler(CheckedException.class)
    public AjaxResult handleCheckedException(Exception e, HttpServletRequest request)
    {
        log.error("请求地址'{}',发生数据异常.",e.getMessage());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 内部认证异常
     */
    @ExceptionHandler(InnerAuthException.class)
    public AjaxResult handleInnerAuthException(InnerAuthException e)
    {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("演示模式，不允许操作");
    }
}
