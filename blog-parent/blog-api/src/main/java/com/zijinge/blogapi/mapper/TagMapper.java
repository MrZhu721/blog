package com.zijinge.blogapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijinge.blogapi.pojo.Tag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章Id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagByArticleId(long articleId);


    /**\
     * 查询最热标签前limit条
     * @param limit
     * @return
     */
    List<Long> findHotsTagId(int limit);

    /**
     * 根据最热标签id查找标签名，返回一个标签对象Tag
     * @param tagIdList
     * @return
     */
    List<Tag> findTagsByTagIds(@Param("tagIds") List<Long> tagIdList);
}
