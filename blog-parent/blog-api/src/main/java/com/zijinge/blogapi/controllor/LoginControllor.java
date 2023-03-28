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
@RequestMapping("/login")
public class LoginControllor {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams loginParams) {

        return loginService.login(loginParams);
    }
}