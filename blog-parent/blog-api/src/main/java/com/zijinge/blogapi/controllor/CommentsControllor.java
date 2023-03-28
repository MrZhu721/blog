package com.zijinge.blogapi.controllor;

import com.zijinge.blogapi.Vo.params.CommentParam;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *评论功能
 */
@RestController
@RequestMapping("/comments")
public class CommentsControllor {

    @Autowired
    private CommentsService commentsService;

    /**
     * 评论列表
     * @param id
     * @return
     */
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id) {

        return commentsService.commentsByArticleId(id);
    }


    /**
     * 评论功能
     * ！！！想要评论需要登录验证才行，所以我们将该接口添加到拦截器
     * @return
     */
    @PostMapping("/create/change")
    public Result comment(CommentParam commentParam) {

        return commentsService.comment(commentParam);
    }

}