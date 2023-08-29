package com.jovefast.common.security.handler;

import com.jovefast.common.core.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * @Description 高优先级的异常处理
 * @Author Acechengui
 * @Date Created in 2023-08-29
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class HighestGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(HighestGlobalExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    public AjaxResult handleSQLException(SQLException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生脚本执行异常.", requestURI, e);
        return AjaxResult.error("发生了数据库异常");
    }
}
