package com.zijinge.blogapi.service;

import com.zijinge.blogapi.Vo.params.CommentParam;
import com.zijinge.blogapi.Vo.result.Result;

public interface CommentsService {

    /**
     * 根据文章id查询评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    /**
     *
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
