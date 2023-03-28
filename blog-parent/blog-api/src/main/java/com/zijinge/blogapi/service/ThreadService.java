package com.zijinge.blogapi.service;

import com.zijinge.blogapi.mapper.ArticleMapper;
import com.zijinge.blogapi.pojo.Article;

public interface ThreadService {
    void updateArticleViewCount(ArticleMapper articleMapper, Article article);
}