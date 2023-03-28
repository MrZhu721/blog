package com.zijinge.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zijinge.blogapi.Vo.TagVo;
import com.zijinge.blogapi.Vo.result.Result;
import com.zijinge.blogapi.mapper.TagMapper;
import com.zijinge.blogapi.pojo.Tag;
import com.zijinge.blogapi.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagByArticleId(long articleId) {

        //mybatis-plus无法进行多表联查
        List<Tag> tagList = tagMapper.findTagByArticleId(articleId);

        return copyList(tagList);
    }

    @Override
    public Result hots(int limit) {

        //  标签拥有的文章最多 = 最热标签
        //  查询 根据 tag_id 分组 计数，从大到小排序，取前 limit 个标签
        List<Long> tagIdList = tagMapper.findHotsTagId(limit);
        if(CollectionUtils.isEmpty(tagIdList)) {    //判断Id是否为空
            return Result.success(Collections.emptyList());
        }

        //  需求是 tag_id 和 tagName 的一个 tag对象
         List<Tag> tagList = tagMapper.findTagsByTagIds(tagIdList);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);   //查询所有文章标签的结果集
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tagList = tagMapper.selectList(queryWrapper);   //查询所有文章标签的结果集
        return Result.success(copyList(tagList));
    }

    @Override
    public Result tagDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo tagVo = copy(tag);
        return Result.success(tagVo);
    }


    //复制结果集做处理
    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag:tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
    private  TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return  tagVo;
    }


}