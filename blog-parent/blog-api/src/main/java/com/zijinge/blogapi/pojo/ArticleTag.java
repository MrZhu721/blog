package com.zijinge.blogapi.pojo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 文章和标签的关联表
 */
@Data
public class ArticleTag {

    // 分布式id 比较长，传到前端 会有精度损失，必须转为string类型 进行传输，就不会有问题了
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long articleId;

    private Long TagId;

}