package com.dimple.project.blog.mapper;

import com.dimple.project.blog.domain.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className: Blog
 * @description: 博客Mapper接口
 * @author: Dimple
 * @date: 2019-10-28
 */
public interface BlogMapper {
    /**
     * 查询博客
     *
     * @param id 博客ID
     * @return 博客
     */
    Blog selectBlogById(Long id);

    /**
     * 查询博客列表
     *
     * @param blog 博客
     * @return 博客集合
     */
    List<Blog> selectBlogList(Blog blog);

    /**
     * 新增博客
     *
     * @param blog 博客
     * @return 结果
     */
    int insertBlog(Blog blog);

    /**
     * 修改博客
     *
     * @param blog 博客
     * @return 结果
     */
    int updateBlog(Blog blog);

    /**
     * 删除博客
     *
     * @param id 博客ID
     * @return 结果
     */
    int deleteBlogById(@Param("id") Long id, @Param("username") String username);

    /**
     * 批量删除博客
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteBlogByIds(@Param("ids") String[] ids, @Param("username") String username);

    /**
     * 获取Blog Tag List
     *
     * @param query tag
     * @return TagList
     */
    List<String> selectBlogTagList(String query);

}
