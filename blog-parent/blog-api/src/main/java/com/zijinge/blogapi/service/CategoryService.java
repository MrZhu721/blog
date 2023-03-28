package com.zijinge.blogapi.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijinge.blogapi.Vo.CategoryVo;
import com.zijinge.blogapi.Vo.result.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 查询所有文章分类
     * @return
     */
    Result findAll();

    /**
     * 导航——查询所有文章分类
     * @return
     */
    Result findAllDetail();

    /**
     * 查询分类文章列表
     * @param id
     * @return
     */
    Result categoriesDetailById(Long id);
}
