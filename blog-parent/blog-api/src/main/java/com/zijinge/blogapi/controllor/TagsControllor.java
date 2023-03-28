package com.zijinge.blogapi.controllor;


import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 最热标签功能
 */

@RestController
@RequestMapping("/tags")
public class TagsControllor {
    @Autowired
    private TagService tagService;

    @GetMapping("/hot") //  url => /tags/hot
    public Result hots() {
        int limit = 6;
        return tagService.hots(limit);
    }

    /**
     * 所有文章标签
     * @return
     */
    @GetMapping
    public Result findAll() {
        return tagService.findAll();
    }

    /**
     * 导航——文章标签
     * @return
     */
    @GetMapping("/detail")
    public Result detail() {

        return tagService.findAllDetail();
    }

    /**
     * 标签文章列表
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result tagDetailById(@PathVariable("id") Long id) {

        return tagService.tagDetailById(id);
    }
}