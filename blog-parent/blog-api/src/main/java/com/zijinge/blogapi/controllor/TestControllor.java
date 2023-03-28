package com.zijinge.blogapi.controllor;

import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.pojo.SysUser;
import com.zijinge.blogapi.utils.UserThreadLocal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestControllor { //测试接口

    @RequestMapping
    public Result test() {
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}