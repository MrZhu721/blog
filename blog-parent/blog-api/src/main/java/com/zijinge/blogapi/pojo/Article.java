package com.zijinge.blogapi.pojo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Article {
    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    //防止前端 精度损失 把id转为string
    // 分布式id 比较长，传到前端 会有精度损失，必须转为string类型 进行传输就不会有问题了
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String summary;

    /**
     * 阅读BUG修正 ：实际就是访问文章详情时将 int 数据类型的属性置0
     * 原先数据类型为 int ,但是在 mybatis-plus 中 int 有一点 就是在设置对象类型参数时，如果不打算某个属性，那么一般时为 null
     * 但是 int 类型默认时有值的 且值为0， 这就导致了mybatis-plus在处理该数据类型时默认传入0为参数，重置了数据库的字段参数
     */
    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private Integer weight;


    /**
     * 创建时间
     */
    private Long createDate;

}