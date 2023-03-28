package com.zijinge.blogapi.controllor;

import com.zijinge.blogapi.Vo.params.LoginParams;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterControlloer {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParams loginParams) {
        // sso 单点登录
        return loginService.register(loginParams);
    }
}