package com.zijinge.blogadmin.service;

import com.zijinge.blogadmin.pojo.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    /**
     * 登录认证功能实现
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * 登录的时候，会把username传递到这里
         * 通过username查询admin表，如果admin存在 就把密码告诉spring Security
         * 如果不存在，就返回null，认证失败了
         */
        // 通过username 查出admin数据
        Admin admin = adminService.findAdminByUsername(username);
        if(admin == null) {
            // 登陆失败
            return null;
        }
        // 创建一个 springSecurity 的 User实现类
        UserDetails userDetails = new User(username,admin.getPassword(),new ArrayList<>());

        return userDetails;
    }
}