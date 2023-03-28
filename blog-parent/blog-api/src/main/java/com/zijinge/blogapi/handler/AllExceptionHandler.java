package com.zijinge.blogapi.handler;


import com.zijinge.blogapi.Vo.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice   // 对加了@Controllor注解的方法进行拦截处理 (AOP的实现)
public class AllExceptionHandler {
    //进行异常处理，处理 Exception.class 的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody   //返回JSON格式数据
    public Result doException(Exception exc) {
        exc.printStackTrace();  //打印堆栈
        return Result.fail(-999,"系统异常");    // 反馈给前端的异常消息
    }
}