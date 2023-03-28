package com.zijinge.blogapi.Vo.params;

import com.zijinge.blogapi.Vo.CategoryVo;
import com.zijinge.blogapi.Vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParams {
    private Long id;

    private ArticleBodyParams body; //文章内容

    private CategoryVo category;    //文章类别

    private String title;   // 文章标题

    private String summary; //文章概述

    private List<TagVo> tags;   // 文章标签
}