package com.zijinge.blogadmin.service;

import com.zijinge.blogadmin.pojo.Admin;
import com.zijinge.blogadmin.pojo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication) {

        /**
         * 权限认证
         */
        // 获取请求路径，用于后续比对
        String requestURI = request.getRequestURI();
        // 获取当前登录用户信息
        Object principal = authentication.getPrincipal();
        // 判断是否为空或者是匿名用户
        if(principal == null || "anonymousUser".equals(principal)) {
            // 未登录
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        // 获取用户名
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUsername(username);
        if(admin == null) {
            return false;
        }
        if(admin.getId() == 1) {
            // 认定为超级管理员 root 直接放行
            return true;
        }
        Integer id = admin.getId();
        List<Permission> permissionList = adminService.findPermissionByAdminId(id);
        // url路径带有 ？ 传参要进行切片操作,得到？之前的路径，也就是下标为零的一串
        requestURI = StringUtils.split(requestURI,"?")[0];
        for (Permission permission:permissionList) {
            // 路径比对进行判断
            if(requestURI.equals(permission.getPath())) {
                return true;
            }
        }
        return false;
    }
}
