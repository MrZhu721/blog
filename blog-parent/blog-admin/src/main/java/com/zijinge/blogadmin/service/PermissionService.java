package com.zijinge.blogadmin.service;

import com.zijinge.blogadmin.pojo.Permission;
import com.zijinge.blogadmin.vo.params.PageParams;
import com.zijinge.blogadmin.vo.result.Result;

public interface PermissionService {
    /**
     * 分页查询数据列表
     * @param pageParams
     * @return
     */
    Result listPermission(PageParams pageParams);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}