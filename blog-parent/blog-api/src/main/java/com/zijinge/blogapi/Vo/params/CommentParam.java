package com.zijinge.blogapi.Vo.params;


import lombok.Data;

/**
 * 创建评论参数对象
 */
@Data
public class CommentParam {
    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}