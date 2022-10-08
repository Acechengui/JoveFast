package com.jovefast.common.security.aspect;

import com.alibaba.fastjson2.JSON;
import com.jovefast.common.core.exception.CheckedException;
import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.common.security.annotation.Assign;
import com.jovefast.common.security.auth.AuthUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Acechengui
 * @description: 基于 Spring Aop 的注解鉴权是否给予属性否赋值
 * @date 2022/9/10 13:04
 */
@Component
@Aspect
public class AssignAspect {

    /**
     * 构建
     */
    public AssignAspect() {
    }

    /**
     * 定义AOP签名 (切入所使用注解的方法)
     */
    public static final String POINTCUT_SIGN = "@annotation(com.jovefast.common.security.annotation.ColAssign)";

    /**
     * 声明AOP签名
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }


    /**
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     *                   a.环绕通知的方法规范
     *                   1.方法权限：public
     *                   2.方法返回值：目标方法的返回值，如果目标方法有返回值可以直接修改
     *                   3.方法名称：自定义
     *                   4.方法参数：目标方法本身
     *                   5.需要使用注解：@Around来指定通知的切入时机
     *                   value参数：用来指定切入点表达式
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //前置通知
        /*result为连接点的放回结果*/
        Object result = null;
        /*执行目标方法*/
        try {
            //joinPoint即为目标方法本身，result为目标方法的返回值，根据反射获取外部调用的目标方法和目标方法需要的参数
            result = joinPoint.proceed();
            /*返回通知方法*/
        } catch (Throwable e) {
            /*异常通知方法*/
            throw new CheckedException(e.getMessage());
        }
        /*后置通知*/
        return result;
    }

    /**
     * @param list 要处理的数据集合
     * @param <T>  返回的数据集合
     * @return 修改后的结果
     */
    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(List<?> ret) throws IllegalAccessException {
        if (!CollectionUtils.isEmpty(ret)) {
            //默认无权限
            boolean flag = false;
            //获取所有字段
            Field[] field = ret.get(0).getClass().getDeclaredFields();
            for (Field f : field) {
                f.setAccessible(true);
                //判断成员变量是否有注解
                if (f.isAnnotationPresent(Assign.class)) {
                    //获取定义在成员变量中的注解
                    Assign as = f.getAnnotation(Assign.class);
                    String[] split = as.roleCallout().split(",");
                    for (String s : split) {
                        if (AuthUtil.hasRole(s)) {
                            //存在有权限的角色
                            flag = true;
                        }
                    }
                }
            }
            //无权限则需要处理
            if (!flag) {
                for (Object item : ret) {
                    processingData(item);
                }
            }
        }
    }
    /**
     * @description: 处理数据
     * @author Acechengui
     */
    private void processingData(Object item) throws IllegalAccessException {
        //获取所有字段
        for (Field fs : item.getClass().getDeclaredFields()) {
            fs.setAccessible(true);
            //判断成员变量是否有注解
            if (fs.isAnnotationPresent(Assign.class)) {
                if (fs.getType() == Double.class) {
                    fs.set(item, 0.00);
                } else if (fs.getType() == Integer.class) {
                    fs.set(item, 0);
                } else {
                    fs.set(item, null);
                }
            }
        }
    }
}
