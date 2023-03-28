package com.zijinge.blogapi.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijinge.blogapi.Vo.params.LoginParams;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.pojo.SysUser;

public interface LoginService  {

    /**
     * 登录功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    /**
     * 退出登录功能
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);
}
