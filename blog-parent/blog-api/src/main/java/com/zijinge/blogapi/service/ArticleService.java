package com.zijinge.blogapi.service;

import com.zijinge.blogapi.Vo.params.ArticleParams;
import com.zijinge.blogapi.Vo.params.PageParams;
import com.zijinge.blogapi.Vo.result.Result;

public interface ArticleService {


    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 分页查询，文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     *  最新文章
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    /**
     *  文章归档
     * @return
     */
    Result listArchives();

    /**
     * 发布文章
     * @param articleParams
     * @return
     */
    Result publish(ArticleParams articleParams);
}