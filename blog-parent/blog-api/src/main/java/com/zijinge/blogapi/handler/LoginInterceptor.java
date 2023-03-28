package com.zijinge.blogapi.handler;

import com.alibaba.fastjson.JSON;
import com.zijinge.blogapi.Vo.ErrorCode;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.pojo.SysUser;
import com.zijinge.blogapi.service.LoginService;
import com.zijinge.blogapi.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component //注入到 spring
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 前端发送请求，后端接收
         * Controllor处理请求之前 执行 preHandle
         */

        // 1.判断请求的接口路径 是否为HandleMethod (Conrtollor)
        // 2.判断token是否为空，为空就是未登录
        // 3.token不为空就验证token （LoginService checkToken）
        // 4.验证成功就放行

        if( !(handler instanceof HandlerMethod)) { // instanceof 测试它左边的对象是否是它右边的类的实例，返回 boolean 的数据类型
            // handler 有可能是 RequestResourceHandler springboot程序的 静态资源访问
            return true;
        }
        String token = request.getHeader("Authorization");  //从请求头拿到token

        // 日志打印
        log.info("=============================== request start ======================================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("=============================== request end ======================================");

        if(StringUtils.isBlank(token)) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录"); //返回错误码
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result)); //转成json格式
            return false;
        }

        SysUser sysUser = loginService.checkToken(token);
        if(sysUser == null) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录"); //返回错误码
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result)); //转成json格式
            return false;
        }
        // 登录验证成功，放行
        // 放行是放行了，如果我还想在Conterollor中 直接获取用户的信息 怎么获取？
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /**
         * 前端发送请求，后端接收
         * Controllor处理请求之后，视图返回渲染之前 执行 postHandle
         */
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /** 前端发送请求，后端接收
         * Controllor流程走完后，视图渲染后 执行 afterCompletion 一般用于干资源清理的事情
         */

        // 如果不删除 ThreadLocal 中用完的信息，就会造成内存泄露风险
        UserThreadLocal.remove();
    }
}