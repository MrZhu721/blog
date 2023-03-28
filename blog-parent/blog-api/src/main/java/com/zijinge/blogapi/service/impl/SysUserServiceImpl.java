package com.zijinge.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zijinge.blogapi.Vo.ErrorCode;
import com.zijinge.blogapi.Vo.LoginUserVo;
import com.zijinge.blogapi.Vo.UserVo;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.mapper.ArticleBodyMapper;
import com.zijinge.blogapi.mapper.ArticleMapper;
import com.zijinge.blogapi.mapper.SysUserMapper;
import com.zijinge.blogapi.pojo.SysUser;
import com.zijinge.blogapi.service.LoginService;
import com.zijinge.blogapi.service.SysUserService;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired  //自动装配redis模板
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private LoginService loginService;

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        //  防止测试数据为空,报空指针
        if(sysUser ==null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("子衿阁");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userVo,sysUser);
        return userVo;
    }

    @Override
    public SysUser findUserByArticleId(Long articleId) {

        SysUser sysUser = sysUserMapper.selectById(articleId);
        //  防止测试数据为空,报空指针
        if(sysUser ==null) {
             sysUser = new SysUser();
             sysUser.setNickname("子衿阁");
        }

        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount,account);
        wrapper.eq(SysUser::getPassword,password);
        wrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        wrapper.last("limit 1");
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public Result findUserByToken(String token) {

        /**
         * 1.token合法性校验
         *  是否为空，解析是否成功，redis是否存在
         * 2.校验失败，返回错误
         * 3.校验成功，返回对应结果 LoginUserVo(包装类)
         */

        SysUser sysUser = loginService.checkToken(token);
        if(sysUser == null) { //判断token是否为空
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickName(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        /**
         * 保存用户 id会自动生成
         * id默认是分布式id，采用雪花算法，来自于 mybatis-plus
         */
        this.sysUserMapper.insert(sysUser);
    }

}