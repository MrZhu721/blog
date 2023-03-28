package com.zijinge.blogadmin.service;

import com.zijinge.blogadmin.pojo.Admin;
import com.zijinge.blogadmin.pojo.Permission;

import java.util.List;

public interface AdminService {

    Admin findAdminByUsername(String username);

    List<Permission> findPermissionByAdminId(Integer id);
}