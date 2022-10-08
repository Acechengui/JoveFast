package com.jovefast.common.security.annotation;

import java.lang.annotation.*;

/**
 * @author Acechengui
 * @description: 需要对哪个属性赋值
 * @date 2022/9/10 16:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ColAssign {

}
