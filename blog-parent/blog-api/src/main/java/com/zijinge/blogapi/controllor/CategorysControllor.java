package com.zijinge.blogapi.controllor;


import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorys")
public class CategorysControllor {

    @Autowired
    private CategoryService categoryService;

    /**
     * 所有文章分类
     * @return
     */
    @GetMapping
    public Result categorysList() {
        return categoryService.findAll();
    }

    /**
     * 导航——文章分类
     * @return
     */
    @GetMapping("/detail")
    public Result detail() {

        return categoryService.findAllDetail();
    }

    /**
     * 分类文章列表
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id) {
        return categoryService.categoriesDetailById(id);
    }
}