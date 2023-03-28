package com.zijinge.blogadmin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {

    @TableId(type = IdType.AUTO)    //id自增
    private Long id;

    private String path;

    private String name;

    private String description;
}