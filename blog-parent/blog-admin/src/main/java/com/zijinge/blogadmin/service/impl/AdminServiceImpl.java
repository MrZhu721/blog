package com.zijinge.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zijinge.blogadmin.mapper.AdminMapper;
import com.zijinge.blogadmin.pojo.Admin;
import com.zijinge.blogadmin.pojo.Permission;
import com.zijinge.blogadmin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findAdminByUsername(String username) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        queryWrapper.last("limit 1");
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public List<Permission> findPermissionByAdminId(Integer adminId) {
        return adminMapper.findPermissionByAdminId(adminId);
    }
}