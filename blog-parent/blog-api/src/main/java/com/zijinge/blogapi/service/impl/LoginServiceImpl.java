package com.zijinge.blogapi.service.impl;


import com.alibaba.fastjson.JSON;
import com.zijinge.blogapi.Vo.ErrorCode;
import com.zijinge.blogapi.Vo.params.LoginParams;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.pojo.SysUser;
import com.zijinge.blogapi.service.LoginService;
import com.zijinge.blogapi.service.SysUserService;
import com.zijinge.blogapi.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional //添加开启事务的注解
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired  //自动装配redis模板
    private RedisTemplate<String,String> redisTemplate;

    private static final String slat = "mszlu!@#";   //设置加密盐

    @Override
    public Result login(LoginParams loginParams) {
        /**
         * 1.检查参数是否合法
         * 2.根据用户名和密码查找是否存在
         * 3.不存在，登录失败
         * 4.存在，使用jwt生成token返回给前端
         * 5.token存入redis当中,redis中记录 token:user信息 的映射关系， 设置过期时间
         * (登录认证时，1.先认证token字符串是否合法，2.再去redis认证token是否存在) 双层保险
         */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)) { //判断参数是否合法
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + slat);  //密码加密
        SysUser sysUser = sysUserService.findUser(account,password);
        if(sysUser == null)  {  //判断用户是否存在
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());   //获得token
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);   //将token存入redis并设置过期时间

        return Result.success(token);   //返回给前端
    }


    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isBlank(token)) {    //1.判断token是否为空
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);   //2.调用JWT工具类校验解析token合法
        if(stringObjectMap == null) {
            return null;
        }
        String userJSON = redisTemplate.opsForValue().get("TOKEN_" + token);    //3.根据token 查找redis是否存在
        if(StringUtils.isBlank(userJSON)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);    //4.通过JSON工具类转换成User对象

        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);     //清除redis缓存里面的token
        return Result.success(null);
    }

    @Override
    public Result register(LoginParams loginParams) {

        /**
         * 1。判断参数是否合法
         * 2. 判断账户是否存在，存在，返回账户已被注册
         * 3. 不存在就注册账户
         * 4. 生成token
         * 5. 存入redis，并返回
         * 6. 注意要加事务，一旦中间的任何流程出现问题，需要让注册的帐户进行回滚
         */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)) { //判断参数是否合法
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        SysUser sysUser = sysUserService.findUserByAccount(account);
        if(sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_ERROR.getCode(),"账号已经被注册了");
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");

        this.sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());   //获得token
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);   //将token存入redis并设置过期时间
        return Result.success(token);   //返回给前端
    }
}