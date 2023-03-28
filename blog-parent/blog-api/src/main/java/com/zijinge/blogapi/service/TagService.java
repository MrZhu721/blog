package com.zijinge.blogapi.service;

import com.zijinge.blogapi.Vo.TagVo;
import com.zijinge.blogapi.Vo.result.Result;

import java.util.List;

public interface TagService {

    List<TagVo> findTagByArticleId(long articleId);

    Result hots(int limit);

    /**
     * 查询所有文章标签
     * @return
     */
    Result findAll();

    /**
     * 导航——查询所有文章标签
     * @return
     */
    Result findAllDetail();

    /**
     * 查询标签文章列表
     * @param id
     * @return
     */
    Result tagDetailById(Long id);
}
