package com.zijinge.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zijinge.blogapi.Vo.ArticleBodyVo;
import com.zijinge.blogapi.Vo.ArticleVo;
import com.zijinge.blogapi.Vo.CategoryVo;
import com.zijinge.blogapi.Vo.TagVo;
import com.zijinge.blogapi.Vo.params.ArticleParams;
import com.zijinge.blogapi.Vo.params.PageParams;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.dos.Archives;
import com.zijinge.blogapi.mapper.ArticleBodyMapper;
import com.zijinge.blogapi.mapper.ArticleMapper;
import com.zijinge.blogapi.mapper.ArticleTagMapper;
import com.zijinge.blogapi.pojo.*;
import com.zijinge.blogapi.service.*;
import com.zijinge.blogapi.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result findArticleById(Long articleId) {
        // 1.根据id查询，文章信息
        // 2.根据bodyid和categoryid 去做关联查询

        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);

        // 在查看文章的同时，并去更新阅读数，能不能做到？
        // 问题1 ： 在查看完文章后，本应该直接返回数据，这时候更新的操作，但是更新的操作会给数据库加写锁，阻塞其他读的操作，从而降低性能
        // 问题2： 如果更新出现问题，不能影响查看文章的操作
        // 解决方法 使用线程池，将更新操作丢到线程池中执行，而且不影响主线程的操作，和主线程就不相关了

        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        List<ArticleVo> articleVoList = copyList(records, true, true);

        return Result.success(articleVoList);
    }


//    @Override
//    public Result listArticle(PageParams pageParams) {
//
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize()); //查询参数
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>(); //查询条件筛查
//        // 分类列表条件筛选
//        if(pageParams.getCategoryId() != null) {
//            // AND category_id = #{categoryId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        // 标签列表 条件筛选
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId() != null) {
//            // Article表中并没有tag字段,因为一篇文章对应多个标签
//            // Article_Tag   article_id 1 = n tag_id
//            LambdaQueryWrapper<ArticleTag> articleTagQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagQueryWrapper);
//            for (ArticleTag articleTag:articleTagList) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if(articleIdList.size() > 0) {
//                // AND id in(1,2,3)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        //  是否置顶排序
//        // queryWrapper.orderByDesc(Article::getWeight); //设置查询条件1 PS:多个条件的先后顺序会影响查询结果
//        //  相当于语句  ==》 order by create_date Desc
//        //  queryWrapper.orderByDesc(Article::getCreateDate); //设置查询条件2
//        //==============================================================================
//        //也可以加入多个条件形成筛查列表
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);       // 是否置顶进行排序
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords(); //得到list结果集
//
//        List<ArticleVo> articleVoList = copyList(records,true,true);  // 结果集不能直接返回，还需要进行处理
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        //  LambdaQueryWrapper  条件构造器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>(); // 创建 查询条件筛查 对象
        queryWrapper.orderByDesc(Article::getViewCounts);   // 根据什么排序
        queryWrapper.select(Article::getId,Article::getTitle);  // 返回查完的哪些参数结果
        queryWrapper.last("limit "+ limit); //查几条

        // select id,title from article order by view_count desc limit #{limit}
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        //  LambdaQueryWrapper  条件构造器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>(); // 创建 查询条件筛查 对象
        queryWrapper.orderByDesc(Article::getCreateDate);   // 根据什么排序
        queryWrapper.select(Article::getId,Article::getTitle);  // 返回查完的哪些参数结果
        queryWrapper.last("limit "+ limit); // 查几条

        // select id,title from article order by create_date desc limit #{limit}
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList= articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result publish(ArticleParams articleParams) {
        /**
         * 1.发布文章就要创建文章对象，也就是article对象
         * 2.作者id,也就是获取当前登录用户的id
         * 3.文章标签，要将标签加入到关联表当中
         * 4.body，内容存储
         */
        Article article = new Article();

        // 此接口要加入到登录拦截器中，否则获取为空，报空指针
        SysUser sysUser = UserThreadLocal.get();
        //作者id
        article.setAuthorId(sysUser.getId());
        // 设置返回属性默认值
        article.setWeight(Article.Article_Common);  // 权重
        article.setViewCounts(0);   // 阅读次数
        article.setTitle(articleParams.getTitle());     // 标题
        article.setSummary(articleParams.getSummary()); // 概述
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParams.getCategory().getId()); // 文章类别
        // 插入之后，会自动生成一个文章id
        articleMapper.insert(article);
        // tag
        List<TagVo> tags = articleParams.getTags();

        for (TagVo tagVo :tags) {
            if( tagVo != null) {
                Long articleId = article.getId();  //获取文章id
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tagVo.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        // body
        ArticleBody articleBody = new ArticleBody();    // new一个内容对象
        articleBody.setArticleId(article.getId());  //文章id
        articleBody.setContent(articleParams.getBody().getContent());   //文章内容
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());   // 文章html样式内容
        articleBodyMapper.insert(articleBody);
        articleBodyMapper.updateById(articleBody);
        //插入后自动生成id,这时候才能去获取id
        article.setBodyId(articleBody.getId());     // 获取内容id   此行代码有问题 !!!
        articleMapper.updateById(article);  //更新

        ArticleVo articleVo = new ArticleVo();  //创建articleVo对象
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }

    //复制list结果集
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        //遍历结果集
        for (Article record:records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));    //调用方法处理每条数据并返回，添加至结果集
        }
        return articleVoList;
    }

    /**
     * 方法重写
     * 不影响上一个方法原有的实现
     */
    //复制list结果集
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        //遍历结果集
        for (Article record:records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));    //调用方法处理每条数据并返回，添加至结果集
        }
        return articleVoList;
    }


    /**
     * 实体类过滤器
     * 由if else 组成的一层层的过滤网
     * 实体类返回上层时，用于筛选掉一些不需要携带的属性
     * @return
     */
    //复制结果集的单条记录并进行数据处理
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

        //由于Article的时间属性和ArticleVo的时间属性数据类型不一致，在复制转移时要修改，重新设置。
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));

        //并不是所有的接口都需要标签（作者和信息）
        if(isTag) {
            Long articleId = article.getId();   //通过文章id去找对应的标签
            articleVo.setTags(tagService.findTagByArticleId(articleId));
        }
        if(isAuthor) {
            Long authorId = article.getAuthorId();  ////通过文章id去找对应的作者id
            articleVo.setAuthor(sysUserService.findUserByArticleId(authorId).getNickname());
        }
        if(isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }

        return articleVo;
    }


    // 通过id查询文章内容
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
//        BeanUtils.copyProperties(articleBody,articleBodyVo); //将前者属性copy给后者
        return articleBodyVo;
    }
}