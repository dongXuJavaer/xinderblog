package com.xinder.article.service;

import com.xinder.api.response.dto.CategoryListDtoResult;

/**
 * @author Xinder
 * @date 2023-01-09 21:50
 */
public interface CategoryService {


    /**
     * 和获取所有分类
     * @return
     */
    CategoryListDtoResult getAllCategories();

}
