package com.zijinge.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zijinge.blogapi.Vo.CommentVo;
import com.zijinge.blogapi.Vo.UserVo;
import com.zijinge.blogapi.Vo.params.CommentParam;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.mapper.CommentMapper;
import com.zijinge.blogapi.pojo.Comment;
import com.zijinge.blogapi.pojo.SysUser;
import com.zijinge.blogapi.service.CommentsService;
import com.zijinge.blogapi.service.SysUserService;
import com.zijinge.blogapi.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章id查询 评论列表 ，从Comment表中查询
         * 2.根据作者的id 查询作者的信息
         * 3.判断 如果 level = 1 要去查询他有没有子评论
         * 4.如果有 根据评论id 进行查询 （parent_id）
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id).eq(Comment::getLevel,1);  //通过文章id查找评论权重为1的评论列表
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(commentList);

        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();    //开启线程

        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());

        // 判断该条评论是父评论还是子评论 并赋予对应的权重
        Long parent = commentParam.getParent(); //获取父id
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }

        comment.setParentId(parent == null ? 0 : parent);
        // 发给谁？还是说就是楼主
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        // 验证通过把该条评论保存到数据库
        this.commentMapper.insert(comment);
        return Result.success(null);
    }

    /**
     * 转换实体类集为Vo集
     */
    private List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment :commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    /**
     * 转换实体类为Vo
     */
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);    //实体类属性copy
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        // 子评论
        if(comment.getLevel() == 1) {    //判断权重
            Long id = comment.getId();  //获取父评论id
            List<CommentVo> commentVoList = findCommentsByParentId(id); //获取子评论列表
            commentVo.setChildrens(commentVoList);  //放入评论列表对象
        }
        // to User 评论对象（给谁评论）
        if(comment.getLevel() > 1) {
            Long toUid = comment.getToUid();
            UserVo touserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(touserVo);
        }
        return commentVo;
    }

    /**
     * 子评论列表查询
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id).eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}