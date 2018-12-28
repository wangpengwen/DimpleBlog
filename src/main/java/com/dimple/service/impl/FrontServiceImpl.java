package com.dimple.service.impl;

import com.dimple.bean.Blog;
import com.dimple.bean.Category;
import com.dimple.repository.BlogRepository;
import com.dimple.repository.CategoryRepository;
import com.dimple.service.FrontService;
import com.dimple.utils.FileOperateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FrontServieImpl
 * @Description:
 * @Auther: Owenb
 * @Date: 12/17/18 15:39
 * @Version: 1.0
 */
@Service
@Transactional
public class FrontServiceImpl implements FrontService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BlogRepository blogRepository;


    @Autowired
    FileOperateUtil fileOperateUtil;

    @Override
    public List<Map<String, Object>> getNewestBlog(Pageable pageable) {
        List<Blog> blogs = blogRepository.findAll(pageable).getContent();
        //将List集合转为Map
        //使用LinkedhashMap保证有序
        List<Map<String, Object>> resultList = new LinkedList<>();
        for (Blog blog : blogs) {
            Map<String, Object> map = new HashMap<>();
            map.put("blogId", blog.getBlogId());
            map.put("title", blog.getTitle());
            map.put("summary", blog.getSummary());
            map.put("categoryName", getCategoryNameById(blog.getCategoryId()));
            map.put("headUrl", blog.getHeaderUrl());
            if (blog.getUpdateTime() == null) {
                map.put("time", new SimpleDateFormat("yyyy-MM-dd").format(blog.getCreateTime()));
            } else {
                map.put("time", new SimpleDateFormat("yyyy-MM-dd").format(blog.getUpdateTime()));
            }
            map.put("author", "边小丰");
            map.put("authorHeader", "/images/lalal");
            resultList.add(map);
        }
        return resultList;
    }

    /**
     * 根据category的id获取name
     *
     * @param categoryId
     * @return
     */
    private String getCategoryNameById(Integer categoryId) {
        Category category = categoryRepository.getOne(categoryId);
        return category == null ? "" : category.getTitle();
    }

    @Override
    public List<List<Blog>> getBlogsInfo() {
        List<Category> allCategoryOrderByWeight = selectCategorySupported();
        int count = 0;
        List<List<Blog>> blogList = new ArrayList<>();
        for (Category category : allCategoryOrderByWeight) {
            Integer categoryId = category.getCategoryId();
            List<Blog> blogs = blogRepository.findAllByCategoryId(categoryId);
            blogList.add(blogs);
        }
        return blogList;
    }

    @Override
    public List<Blog> getBlogsPeopleSee() {
        //从缓存中获取Blog，如果不够8个，那么就从数据库中去获取
        //这里全部从数据库中获取
        Pageable pageable = PageRequest.of(0, 8);
        return blogRepository.findAll(pageable).getContent();
    }

    @Override
    public List<String> selectCategoryNameToDisplay() {
        List<Category> categories = selectCategorySupported();
        List<String> categoryNames = new LinkedList<>();

        int i = categories.size() < 5 ? categories.size() : 5;
        for (int j = 0; j < i; j++) {
            categoryNames.add(categories.get(j).getTitle());
        }
        return categoryNames;
    }

    private List<Category> selectCategorySupported() {
        return categoryRepository.findAllBySupportEquals(true);
    }

    @Override
    public Map<String, List<Blog>> getCategoryInfo() {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "weight");
        List<Category> categories = categoryRepository.findAll(pageable).getContent();


        //如果数据库中的分类的个数小于5个就按照实际的来，否则只截取5个到前台展示
        int i = categories.size() < 5 ? categories.size() : 5;
        Map<String, List<Blog>> mapMap = new LinkedHashMap<>();
        //根据分类来进行遍历，如果分类的title作为key，其下面的blog的title，id，summary组成一个map作为value
        // for (int j = 0; j < i; j++) {
        //     Category category = categories.get(j);
        //     Integer categoryId = category.getCategoryId();
        //     String title = category.getTitle();
        //     //查询当前分类Id下的所有博客
        //     BlogExample blogExample = new BlogExample();
        //     BlogExample.Criteria criteria = blogExample.createCriteria();
        //     //对博客进行排序，排序的字段为wight
        //     blogExample.setOrderByClause("weight desc");
        //     criteria.andCategoryIdEqualTo(categoryId);
        //     List<Blog> blogs = blogMapper.selectByExample(blogExample);
        //
        //
        //
        //     int kk = blogs.size() < 5 ? blogs.size() : 5;
        //     List<Blog> blogList = new LinkedList<>();
        //     for (int k = 0; k < kk; k++) {
        //         blogList.add(blogs.get(k));
        //     }
        //     mapMap.put(title, blogList);
        // }
        return mapMap;
    }

    @Override
    public Blog getBlog(Integer id) {
        if (id == null) {
            return null;
        }
        Blog blog = blogRepository.getOne(id);
        return fileOperateUtil.generateWrapBlog(blog);
    }

    @Override
    public List<Blog> getClickBlog() {
        Pageable pageable = PageRequest.of(0, 8, Sort.Direction.DESC, "click");
        List<Blog> blogs = blogRepository.findAll(pageable).getContent();
        return fileOperateUtil.generateWrapBlog(blogs);
    }

    @Override
    public List<Blog> getSupportBlog() {
        List<Blog> blogs = blogRepository.findAllBySupportEquals(true);
        //截取
        List<Blog> result = blogs.size() <= 8 ? blogs : blogs.subList(0, 8);
        return fileOperateUtil.generateWrapBlog(result);
    }

    @Override
    public Map<String, Object> getBlogOtherInfo(Integer id) {
        //获取上下一篇
        // Blog nextBlog = customMapper.getNextBlog(id);
        // Blog previousBlog = customMapper.getPreviousBlog(id);
        return null;
    }
}