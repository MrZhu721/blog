package com.zijinge.blogapi.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CategoryVo {

    // 分布式id 比较长，传到前端 会有精度损失，必须转为string类型 进行传输，就不会有问题了
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}