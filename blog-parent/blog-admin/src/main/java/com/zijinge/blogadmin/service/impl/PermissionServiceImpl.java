package com.zijinge.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zijinge.blogadmin.mapper.PermissionMapper;
import com.zijinge.blogadmin.pojo.Permission;
import com.zijinge.blogadmin.service.PermissionService;
import com.zijinge.blogadmin.vo.params.PageParams;
import com.zijinge.blogadmin.vo.result.PageResult;
import com.zijinge.blogadmin.vo.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Result listPermission(PageParams pageParams) {
        /**
         * 要的数据，管理台 表的所有字段 Permission
         * 分页查询
         */
        // 创建分页对象
        Page<Permission> page = new Page<>(pageParams.getCurrentPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(pageParams.getQueryString())) {
            queryWrapper.eq(Permission::getName,pageParams.getQueryString());
        }
        Page<Permission> permissionPage = permissionMapper.selectPage(page,queryWrapper);
        // 创建一个返回前台所需的 分页对象
        PageResult<Permission> pageResult = new PageResult<>();
        // 存入前面查询出来的参数
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());

        return Result.success(pageResult);
    }

    @Override
    public Result add(Permission permission) {
        permissionMapper.insert(permission);
        return Result.success(null);
    }

    @Override
    public Result update(Permission permission) {
        permissionMapper.updateById(permission);
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        permissionMapper.deleteById(id);
        return Result.success(null);
    }
}