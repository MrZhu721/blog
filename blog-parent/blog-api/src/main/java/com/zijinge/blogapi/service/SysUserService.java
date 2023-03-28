package com.zijinge.blogapi.service;

import com.zijinge.blogapi.Vo.UserVo;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.pojo.SysUser;

public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findUserByArticleId(Long articleId);

    SysUser findUser(String account, String password);

    //  根据token查用户信息
    Result findUserByToken(String token);

    // 根据账户查找用户
    SysUser findUserByAccount(String account);

    // 保存用户
    void save(SysUser sysUser);

}
