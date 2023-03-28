package com.zijinge.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.zijinge.blogapi.mapper.ArticleMapper;
import com.zijinge.blogapi.pojo.Article;
import com.zijinge.blogapi.service.ThreadService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl implements ThreadService {

    // 期望此操作在线程池中，从而不会影响主线程的操作
    @Override
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        /**
         * 将 更新阅读次数 的功能放在线程池里，将其与主线程的 文章详情功能隔离开
         * 保证了在 更新阅读 功能出现错误时 不会阻塞主线程的执行
         */
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        // 设置一个 为了在多环境下 线程安全
        updateWrapper.eq(Article::getViewCounts,viewCounts);    //采用了乐观锁机制

        articleMapper.update(articleUpdate,updateWrapper);

        try {
            Thread.sleep(5000);
            System.out.println("更新完成了.....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}