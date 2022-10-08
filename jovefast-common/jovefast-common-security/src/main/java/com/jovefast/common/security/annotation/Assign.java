package com.jovefast.common.security.annotation;

import java.lang.annotation.*;

/**
 * @author Acechengui
 * @Description 运行时为Bean字段是否有权限赋值
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Assign {

    String value() default "";
    /**
     * 需要具备的角色字符,多个只需要用 逗号 分割例如 it,admin
     */
    String roleCallout() default "";
}
