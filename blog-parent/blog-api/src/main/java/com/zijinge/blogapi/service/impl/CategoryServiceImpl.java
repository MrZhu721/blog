package com.zijinge.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zijinge.blogapi.Vo.CategoryVo;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.mapper.CategoryMapper;
import com.zijinge.blogapi.pojo.Category;
import com.zijinge.blogapi.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    // 通过id查询文章类别
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo); //将前者属性copy给后者
        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);    //查询出文章类别结果集
        //  转换成与页面交互的Vo对象

        List<CategoryVo> categoryVoList = copyList(categoryList);
        return Result.success(categoryVoList);
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);    //查询出文章类别结果集
        //  转换成与页面交互的Vo对象

        List<CategoryVo> categoryVoList = copyList(categoryList);
        return Result.success(categoryVoList);
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }

    //  copyList
    public List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category:categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    // copy
    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}