package com.zijinge.blogapi.common.cache;


import java.lang.annotation.*;

/**
 * 创建自定义注解必需要使用下面三个 ’元注解‘
 * 自定义缓存注解
 */
@Target({ElementType.METHOD})   // Type 代表可以放在类上，Method 代表可以放在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    // 过期时间
    long expire() default 1 * 60 * 1000;
    //缓存标识 key
    String name() default "";
}