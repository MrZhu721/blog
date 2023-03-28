package com.zijinge.blogapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zijinge.blogapi.dos.Archives;
import com.zijinge.blogapi.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
//    文章归档
    List<Archives> listArchives();

    /*
    注意使用 @Param 进行参数传递
     */
    IPage<Article> listArticle( Page<Article> page,
                               @Param("categoryId") Long categoryId,
                               @Param("tagId") Long tagId,
                               @Param("year") String year,
                               @Param("month") String month);
}
