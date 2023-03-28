package com.zijinge.blogapi.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.util.List;

@Data
public class CommentVo {
    // 分布式id 比较长，传到前端 会有精度损失，必须转为string类型 进行传输，就不会有问题了
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private UserVo author; //作者

    private UserVo toUser;  //评论对象

    private String content; //内容

    private List<CommentVo> childrens;  //子评论

    private String createDate;  //创建日期

    private Integer level;  //权重
}