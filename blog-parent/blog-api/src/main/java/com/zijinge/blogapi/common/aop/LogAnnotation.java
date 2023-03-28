package com.zijinge.blogapi.common.aop;

import org.apache.ibatis.plugin.Intercepts;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 一个日志注解
 */
@Target({ElementType.METHOD})   // Type 代表可以放在类上，Method 代表可以放在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operator() default "";
}