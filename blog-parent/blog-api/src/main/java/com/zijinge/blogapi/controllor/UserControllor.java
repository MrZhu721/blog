package com.zijinge.blogapi.controllor;

import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserControllor {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(token);
    }
}