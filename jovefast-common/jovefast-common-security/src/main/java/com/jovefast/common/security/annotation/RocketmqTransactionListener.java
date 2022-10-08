package com.jovefast.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义一个RocketmqTransactionListener注解  为了方便定义哪些消息需要实现事务
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketmqTransactionListener {
    String topic();

    String selectorExpression();
}