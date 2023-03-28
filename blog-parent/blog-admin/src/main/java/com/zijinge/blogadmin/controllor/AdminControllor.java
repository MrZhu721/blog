package com.zijinge.blogadmin.controllor;

import com.zijinge.blogadmin.pojo.Permission;
import com.zijinge.blogadmin.service.AdminService;
import com.zijinge.blogadmin.service.PermissionService;
import com.zijinge.blogadmin.vo.params.PageParams;
import com.zijinge.blogadmin.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminControllor {

    @Autowired
    private AdminService adminService;
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission/permissionList")
    public Result listPermission(@RequestBody PageParams pageParams) {

        return permissionService.listPermission(pageParams);
    }


    @PostMapping("/permission/add")
    public Result add(@RequestBody Permission permission) {

        return permissionService.add(permission);
    }

    @PostMapping("/permission/update")
    public Result update(@RequestBody Permission permission) {

        return permissionService.update(permission);
    }


    @GetMapping("/permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {

        return permissionService.delete(id);
    }
}