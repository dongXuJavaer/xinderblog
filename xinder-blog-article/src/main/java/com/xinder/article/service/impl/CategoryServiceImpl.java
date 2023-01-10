package com.xinder.article.service.impl;

import com.xinder.api.bean.Category;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.CategoryListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.article.mapper.CategoryMapper;
import com.xinder.article.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sang on 2017/12/19.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public CategoryListDtoResult getAllCategories() {
        List<Category> categoryList = categoryMapper.getAllCategories();
        CategoryListDtoResult categoryListDtoResult = DtoResult.DataDtoSuccess(CategoryListDtoResult.class);
        categoryListDtoResult.setList(categoryList);
        return categoryListDtoResult;
    }

    public boolean deleteCategoryByIds(String ids) {
        String[] split = ids.split(",");
        int result = categoryMapper.deleteCategoryByIds(split);
        return result == split.length;
    }

    public int updateCategoryById(Category category) {
        return categoryMapper.updateCategoryById(category);
    }

    public int addCategory(Category category) {
        category.setDate(new Timestamp(System.currentTimeMillis()));
        return categoryMapper.addCategory(category);
    }
}
