package com.zijinge.blogapi.controllor;

import com.zijinge.blogapi.Vo.params.ArticleParams;
import com.zijinge.blogapi.Vo.params.PageParams;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.common.aop.LogAnnotation;
import com.zijinge.blogapi.common.cache.Cache;
import com.zijinge.blogapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章列表功能
 */

@RestController //  json数据进行交互
@RequestMapping("/articles")
public class ArticleControllor {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "模式",operator = "获取文章列表")   //开启自定义日志注解     实现日志打印
    @Cache(expire = 5 * 60 * 1000,name = "list_article")
    public Result article(@RequestBody PageParams pageParams) {
//        int i=10/0;   异常测试用例
        Result listArticle = articleService.listArticle(pageParams);
        return listArticle;
    }

    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("/hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle() {
        int limit = 5;
        Result hotArticle = articleService.hotArticle(limit);
        return hotArticle;
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("/new")
    @Cache(expire = 5 * 60 * 1000,name = "new_article")
    public Result newArticle() {
        int limit = 5;
        Result hotArticle = articleService.newArticle(limit);
        return hotArticle;
    }

    /**
     * 首页 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives() {
        Result hotArticle = articleService.listArchives();
        return hotArticle;
    }

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {

        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     * @return
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParams articleParams) {

        return articleService.publish(articleParams);
    }
}