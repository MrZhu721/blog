package com.zijinge.blogapi.common.cache;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zijinge.blogapi.Vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;

@Component
@Aspect //增强（切面）  主要定义了通知和切点的关系
@Slf4j
public class CacheAspect {

    // Redis模板
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 切点
    @Pointcut("@annotation(com.zijinge.blogapi.common.cache.Cache)")
    public void pt(){};

    // 增强
    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
        Signature signature = joinPoint.getSignature();
        // 拿到使用该自定义注解  （@Cache） 对应的类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        // 拿到使用该自定义注解  （@Cache） 对应的方法名
        String methodName = signature.getName();

        // 获取参数
        Object[] args = joinPoint.getArgs();
        Class[] parameterTypes = new Class[args.length];
        // 参数
        String params = "";
        for (int i = 0; i < args.length ; i++) {
            // 判断参数是否为空
            if(args[i] != null) {
                params += JSON.toJSONString(args[i]);    // JSON数据格式化
                parameterTypes[i] = args[i].getClass();
            }else {
                parameterTypes[i] = null;
            }
        }
        if(StringUtils.isNotEmpty(params)) {
            // 以防出现key过长 以及 字符串转义获取不到的情况
            params = DigestUtils.md5Hex(params);
        }
        Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
        // 获取Cache注解
        Cache annotation = method.getAnnotation(Cache.class);
        // 缓存过期时间
        long expire = annotation.expire();
        // 缓存名称
        String name = annotation.name();
        // 先从 redis 获取     Key = 参数名称+类名+方法名+加密后的参数 组成一个 唯一标识
        String redisKey = name + "::" + className + "::" + methodName + "::" + params;
        // 使用 redisTemplate 通过 key 拿到相对应的 value
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if(StringUtils.isNotEmpty(redisValue)) {
            log.info("走了缓存——————,{},{}",className,methodName);
            return JSON.parseObject(redisValue, Result.class);
        }
        // 如果value为空,那就调用方法
        Object proceed = joinPoint.proceed();

        // 将方法返回的结果转换成 JSON 存入到 redis 中，并设置过期时间 Duration.ofMillis()

            redisTemplate.opsForValue().set(redisKey,new ObjectMapper().writeValueAsString(proceed), Duration.ofMillis(expire));
        log.info("存入缓存——————,{},{}",className,methodName);
        return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Result.fail(-999,"系统错误");
    }

}
